package com.reactiveandroid.database;

import com.reactiveandroid.Model;
import com.reactiveandroid.annotation.Database;
import com.reactiveandroid.database.migration.Migration;
import com.reactiveandroid.database.migration.MigrationContainer;
import com.reactiveandroid.serializer.TypeSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains configuration for database
 */
public class DatabaseConfig {

    public final Class<?> databaseClass;
    public final String databaseName;
    public final int databaseVersion;
    public final List<Class<? extends Model>> modelClasses;
    public final List<Class<? extends TypeSerializer>> typeSerializers;
    public final MigrationContainer migrationContainer;
    public final boolean requireMigration;

    public DatabaseConfig(Class<?> databaseClass,
                          String databaseName,
                          int databaseVersion,
                          List<Class<? extends Model>> modelClasses,
                          List<Class<? extends TypeSerializer>> typeSerializers,
                          MigrationContainer migrationContainer,
                          boolean requireMigration) {
        this.databaseClass = databaseClass;
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
        this.modelClasses = modelClasses;
        this.typeSerializers = typeSerializers;
        this.migrationContainer = migrationContainer;
        this.requireMigration = requireMigration;
    }

    public boolean isValid() {
        return modelClasses != null && modelClasses.size() > 0;
    }

    public static class Builder {

        private Class<?> databaseClass;
        private String databaseName;
        private int databaseVersion;
        private List<Class<? extends Model>> modelClasses;
        private List<Class<? extends TypeSerializer>> typeSerializers;
        private MigrationContainer migrationContainer;
        private boolean requireMigration = false;

        public Builder(Class<?> databaseClass) {
            if (!databaseClass.isAnnotationPresent(Database.class)) {
                throw new IllegalArgumentException("Annotation @Database not found for class " + databaseClass.getName());
            }

            Database databaseAnnotation = databaseClass.getAnnotation(Database.class);

            this.databaseClass = databaseClass;
            this.databaseName = databaseAnnotation.name();
            this.databaseVersion = databaseAnnotation.version();
            this.modelClasses = new ArrayList<>();
            this.typeSerializers = new ArrayList<>();
            this.migrationContainer = new MigrationContainer();
            this.requireMigration = false;
        }

        public Builder setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder setDatabaseVersion(int databaseVersion) {
            this.databaseVersion = databaseVersion;
            return this;
        }

        public Builder addModelClasses(Class<? extends Model>... modelClasses) {
            this.modelClasses.addAll(Arrays.asList(modelClasses));
            return this;
        }

        public Builder addTypeSerializers(Class<? extends TypeSerializer>... typeSerializers) {
            this.typeSerializers.addAll(Arrays.asList(typeSerializers));
            return this;
        }

        public Builder addMigrations(Migration... migrations) {
            this.migrationContainer.addMigrations(migrations);
            return this;
        }

        public Builder setRequireMigration(boolean requireMigration) {
            this.requireMigration = requireMigration;
            return this;
        }

        public DatabaseConfig build() {

            return new DatabaseConfig(databaseClass,
                    databaseName,
                    databaseVersion,
                    modelClasses,
                    typeSerializers,
                    migrationContainer,
                    requireMigration);
        }

    }

}