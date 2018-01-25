package org.mypico.jpico.test.db;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mypico.jpico.data.DataFactory;
import org.mypico.jpico.data.pairing.PairingAccessor;
import org.mypico.jpico.db.DbDataFactory;
import org.mypico.jpico.db.DbPairingAccessor;
import org.mypico.jpico.db.DbPairingImp;
import org.mypico.jpico.db.DbServiceImp;
import org.mypico.jpico.test.data.pairing.PairingAccessorTest;
import org.mypico.jpico.test.util.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DbPairingAccessorTest extends PairingAccessorTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(
            DbPairingAccessorTest.class.getSimpleName());

    private static ConnectionSource dbConnection;
    private static DbDataFactory factory;
    private static DbPairingAccessor accessor;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Get a connection to the database
        LOGGER.debug("Connecting to the database...");
        dbConnection = DatabaseHelper.getConnection();
        LOGGER.info("Connected to database");

        factory = new DbDataFactory(dbConnection);
        Dao<DbPairingImp, Integer> pairingDao =
                DaoManager.createDao(dbConnection, DbPairingImp.class);
        accessor = new DbPairingAccessor(pairingDao);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Create database tables
        LOGGER.debug("Creating database tables...");
        TableUtils.createTable(dbConnection, DbServiceImp.class);
        TableUtils.createTable(dbConnection, DbPairingImp.class);
        LOGGER.info("Database tables created");
    }

    @After
    public void tearDown() throws Exception {
        // Delete database tables
        LOGGER.debug("Deleting database tables...");
        TableUtils.dropTable(dbConnection, DbPairingImp.class, true);
        TableUtils.dropTable(dbConnection, DbServiceImp.class, true);
        LOGGER.info("Database tables deleted");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Close database connection
        LOGGER.debug("Closing database connection");
        dbConnection.close();
        LOGGER.info("Closed database connection");
    }

    @Override
    protected DataFactory getFactory() {
        return factory;
    }

    @Override
    protected PairingAccessor getAccessor() {
        return accessor;
    }
}
