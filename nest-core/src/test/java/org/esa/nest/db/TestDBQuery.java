package org.esa.nest.db;

import junit.framework.TestCase;
import org.esa.nest.datamodel.AbstractMetadata;

import java.io.IOException;
import java.sql.SQLException;


/**

 */
public class TestDBQuery extends TestCase {

    private ProductDB db;

    public TestDBQuery(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();

        db = ProductDB.instance();
        final boolean connected = db.connect();
        if(!connected) {
            throw new IOException("Unable to connect to database\n"+db.getLastSQLException().getMessage());
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();

        db.disconnect();
        ProductDB.deleteInstance();
    }

    public void testQuery() throws SQLException {
        final DBQuery dbQuery = new DBQuery();
        dbQuery.setSelectedMissions(new String[] { "ENVISAT"});

        final ProductEntry[] productEntryList = dbQuery.queryDatabase(db);
        showProductEntries(productEntryList);
    }

    public void testFreeQuery() throws SQLException {
        final DBQuery dbQuery = new DBQuery();
        dbQuery.setFreeQuery(AbstractMetadata.PRODUCT+" LIKE 'RS2_SGF%'");

        final ProductEntry[] productEntryList = dbQuery.queryDatabase(db);
        showProductEntries(productEntryList);
    }

    private void showProductEntries(final ProductEntry[] productEntryList) {
        for(ProductEntry entry : productEntryList) {
            System.out.println(entry.getId() +" "+ entry.getName());
        }
    }
}