package com.escape.plan.rednit;

import org.apache.iceberg.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.types.Types;

public class IcebergService {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.s3a.endpoint", "http://localhost:9000");
        conf.set("fs.s3a.access.key", "admin");
        conf.set("fs.s3a.secret.key", "admin123");
        conf.set("fs.s3a.path.style.access", "true");

        // Initialize Iceberg catalog
        HadoopCatalog catalog = new HadoopCatalog(conf, "s3a://iceberg-bucket");

        // Define schema
        Schema schema = new Schema(
                Types.NestedField.required(1, "id", Types.StringType.get()),
                Types.NestedField.optional(2, "title", Types.StringType.get()),
                Types.NestedField.optional(3, "content", Types.StringType.get()),
                Types.NestedField.optional(4, "created_at", Types.TimestampType.withZone())
        );

        // Define partitioning
        PartitionSpec spec = PartitionSpec.builderFor(schema)
                .identity("id")
                .month("created_at")
                .build();

        // Create table
        TableIdentifier tableId = TableIdentifier.of("blog_posts");
        Table table = catalog.createTable(tableId, schema, spec);

        System.out.println("Iceberg table created at: " + table.location());

    }
}
