package com.escape.plan.rednit;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.*;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.data.parquet.GenericParquetWriter;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.*;
import org.apache.iceberg.parquet.Parquet;

import java.io.IOException;
import java.time.*;
import java.util.UUID;

public class IcebergPartitionedWriter {
    public static void main(String[] args) throws IOException {
        Configuration hadoopConf = new Configuration();
        hadoopConf.set("fs.s3a.endpoint", "http://127.0.0.1:9000");
        hadoopConf.set("fs.s3a.access.key", "admin");
        hadoopConf.set("fs.s3a.secret.key", "admin123");
        hadoopConf.set("fs.s3a.path.style.access", "true");

        // Define Iceberg Catalog
        String warehousePath = "s3a://iceberg-bucket";
        HadoopCatalog catalog = new HadoopCatalog(hadoopConf, warehousePath);
        TableIdentifier tableIdentifier = TableIdentifier.of( "blog_posts");
        Table table = catalog.loadTable(tableIdentifier);
        Schema schema = table.schema();

        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

        String id = "1";
        // Create a new Iceberg record
        Record record = GenericRecord.create(schema);
        record.setField("id", "1");
        record.setField("title", "Partitioned Insert");
        record.setField("content", "This goes into a different partition.");
        record.setField("created_at", offsetDateTime);

        // Determine partition (YYYY-MM format)
        LocalDate partitionDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
        String partitionValue = partitionDate.getYear() + "-" + String.format("%02d", partitionDate.getMonthValue());

        // Create Iceberg data file writer
        OutputFileFactory fileFactory = OutputFileFactory.builderFor(table, 1, 1)
                .format(FileFormat.PARQUET)
                .build();
        OutputFile outputFile = fileFactory.newOutputFile().encryptingOutputFile();

        FileAppender<Record> appender = Parquet.write(outputFile)
                .schema(schema)
                .createWriterFunc(GenericParquetWriter::buildWriter)
                .build();

        appender.add(record);
        appender.close();

        PartitionData partitionData = new PartitionData(table.spec().partitionType());
        YearMonth yearMonth = YearMonth.of(offsetDateTime.getYear(), offsetDateTime.getMonth());
        partitionData.set(0, id);
        partitionData.set(1, (yearMonth.getYear() * 12) + yearMonth.getMonthValue() - 1);  // ✅ "created_at_month=2025-03"

        // Register the new data file in Iceberg (automatically places it in the correct partition)
        DataFile dataFile = DataFiles.builder(table.spec())
                .withPath(outputFile.location())
                .withFormat(FileFormat.PARQUET)
                .withPartition(partitionData)
                .withFileSizeInBytes(outputFile.toInputFile().getLength())
                .withRecordCount(1)
                .build();

        table.newAppend().appendFile(dataFile).commit();

        System.out.println("✅ Data successfully inserted into partition: " + partitionValue);

        catalog.close();
    }
}
