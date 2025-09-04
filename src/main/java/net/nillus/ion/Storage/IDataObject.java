package net.nillus.ion.Storage;

import java.sql.SQLException;

public interface IDataObject {
    void INSERT(DatabaseClient dbClient) throws Exception;
    void DELETE(DatabaseClient dbClient) throws SQLException;

    void UPDATE(DatabaseClient dbClient) throws Exception;
}