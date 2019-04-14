package my.base.db;

import java.util.ArrayList;

public interface DbConstants {
    String getDatabaseName();

    int getDatabaseVersion();

    ArrayList<POJO> getTables();
}
