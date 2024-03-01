package org.wsd.app.support;

import com.github.ludoviccarretti.options.PropertiesOptions;
import com.github.ludoviccarretti.services.PostgresqlExportService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Configuration
public class DatabaseBackupSupport {

    @Scheduled(cron = "*/30 * * * * *")
    public void dbBackup() throws SQLException, IOException, ClassNotFoundException {
        //required properties for exporting of db
        Properties properties = new Properties();
        properties.setProperty(PropertiesOptions.DB_NAME, "db");
        properties.setProperty(PropertiesOptions.DB_USERNAME, "user");
        properties.setProperty(PropertiesOptions.DB_PASSWORD, "password");
        properties.setProperty(PropertiesOptions.PRESERVE_GENERATED_ZIP,"true");

//        //properties relating to email config
//        properties.setProperty(PropertiesOptions.EMAIL_HOST, "smtp.mailtrap.io");
//        properties.setProperty(PropertiesOptions.EMAIL_PORT, "25");
//        properties.setProperty(PropertiesOptions.EMAIL_USERNAME, "mailtrap-username");
//        properties.setProperty(PropertiesOptions.EMAIL_PASSWORD, "mailtrap-password");
//        properties.setProperty(PropertiesOptions.EMAIL_FROM, "test@smattme.com");
//        properties.setProperty(PropertiesOptions.EMAIL_TO, "backup@smattme.com");

        //set the outputs temp dir
        properties.setProperty(PropertiesOptions.TEMP_DIR, createBackupDirectory());

        PostgresqlExportService postgresqlExportService = new PostgresqlExportService(properties);
        String generatedSql = postgresqlExportService.getGeneratedSql();
        System.out.println(generatedSql);
        postgresqlExportService.export();
    }

    private static String createBackupDirectory() {
        // Get current timestamp and date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());

        // Create directory path based on timestamp
        String backupDir = "external/" + timestamp;

        // Create directory if it doesn't exist
        File directory = new File(backupDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create parent directories if needed
        }

        return backupDir;
    }

}
