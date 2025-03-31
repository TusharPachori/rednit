package com.escape.plan.rednit;

import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.*;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.expressions.Expressions;
import org.apache.iceberg.hadoop.HadoopCatalog;
import org.apache.iceberg.io.CloseableIterable;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class IcebergFileReader {
    public static void main(String[] args) throws IOException {

    }
    public void fetchFile() {
        Configuration conf = new Configuration();
        conf.set("fs.s3a.endpoint", "http://localhost:9000");
        conf.set("fs.s3a.access.key", "admin");
        conf.set("fs.s3a.secret.key", "admin123");
        conf.set("fs.s3a.path.style.access", "true");

        // Initialize Iceberg catalog
        HadoopCatalog catalog = new HadoopCatalog(conf, "s3a://iceberg-bucket");
        Table table = catalog.loadTable(TableIdentifier.of( "blog_posts"));

// Find the partition's file using Iceberg metadata
        Snapshot snapshot = table.currentSnapshot();
        if (snapshot != null) {
            // ✅ Find all data files matching a partition
            List<ManifestFile> manifests = snapshot.allManifests(table.io());

            for (ManifestFile manifest : manifests) {
                // ✅ Step 3: Read each manifest and iterate over data files
                try (ManifestReader<DataFile> reader = ManifestFiles.read(manifest, table.io())) {
                    Iterator<DataFile> iterator = reader.iterator();  // ✅ Use iterator instead of entries()

                    while (iterator.hasNext()) {
                        DataFile file = iterator.next();

                        // Extract partition values
                        StructLike partition = file.partition();
                        Integer createdAt = partition.get(1, Integer.class); // Adjust index as needed
                        String id = partition.get(0, String.class); // Adjust index as needed

                        // ✅ Check if partition values match
                        // convert date to number and then check in partition
                        if (createdAt==24302 && "1".equals(id)) {
                            System.out.println("Matching file found: " + file.path());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

