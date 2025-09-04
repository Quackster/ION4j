package net.nillus.ion.HabboHotel.Habbos;

import net.nillus.ion.IonEnvironment;
import net.nillus.ion.Storage.DatabaseClient;
import net.nillus.ion.Storage.IDataObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Habbo implements IDataObject {
    // Account
    private int mID;
    private String mUsername;
    private String mPassword;
    private Date mSignedUp;

    // Personal
    private String mEmail;
    private String mDateOfBirth;

    // Avatar
    private String mMotto;
    private String mFigure;
    private char mGender;

    // Valueables
    private int mCoins;
    private int mFilms;
    private int mGameTickets;

    // Properties
    public int getID() {
        return mID;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public Date getSignedUp() {
        return mSignedUp;
    }

    public void setSignedUp(Date signedUp) {
        this.mSignedUp = signedUp;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.mDateOfBirth = dateOfBirth;
    }

    public String getMotto() {
        return mMotto;
    }

    public void setMotto(String motto) {
        // TODO: swear word filtering?
        this.mMotto = motto;
    }

    public String getFigure() {
        return mFigure;
    }

    public void setFigure(String figure) {
        this.mFigure = figure;
    }

    public char getGender() {
        return mGender;
    }

    public void setGender(char gender) {
        this.mGender = (gender == 'M' || gender == 'F') ? gender : 'M';
    }

    public long getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        this.mCoins = coins;
    }

    public long getFilms() {
        return mFilms;
    }

    public void setFilms(int films) {
        this.mFilms = films;
    }

    public long getGameTickets() {
        return mGameTickets;
    }

    public void setGameTickets(int gameTickets) {
        this.mGameTickets = gameTickets;
    }

    // Methods
    public String toProtocolString() {
        return "name=" + mUsername + "\r\n" +
               "figure=" + mFigure + "\r\n" +
               "sex=" + mGender + "\r\n" +
               "customData=" + mMotto + "\r\n" +
               "ph_tickets=" + mGameTickets + "\r\n" +
               "photo_film=" + mFilms + "\r\n" +
               "ph_figure=" + "" + "\r\n" +
               "directMail=0\r\n";
    }

    // Storage
    private void addUserParams(PreparedStatement stmt) throws Exception {
        stmt.setLong(1, mID);
        stmt.setString(2, mUsername);
        stmt.setString(3, mPassword);
        stmt.setDate(4, new java.sql.Date(mSignedUp.getTime()));

        stmt.setString(5, mEmail);
        stmt.setString(6, mDateOfBirth);

        stmt.setString(7, mMotto);
        stmt.setString(8, mFigure);
        stmt.setString(9, String.valueOf(mGender));

        stmt.setLong(10, mCoins);
        stmt.setLong(11, mFilms);
        stmt.setLong(12, mGameTickets);
    }

    private boolean setUserParams(ResultSet rs) throws Exception {
        if (rs == null || !rs.next()) {
            return false;
        }

        mID = rs.getInt("id");
        mUsername = rs.getString("username");
        mPassword = rs.getString("password");
        mSignedUp = rs.getDate("signedup");

        mEmail = rs.getString("email");
        mDateOfBirth = rs.getString("dob");

        mMotto = rs.getString("motto");
        mFigure = rs.getString("figure");
        setGender(rs.getString("gender").charAt(0));

        mCoins = rs.getInt("coins");
        mFilms = rs.getInt("films");
        mGameTickets = rs.getInt("gametickets");

        return true;
    }

    public boolean loadByID(Connection connection, int ID) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ? LIMIT 1;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                setUserParams(result);
            }
        }
        return true;
    }

    public boolean loadByUsername(Connection connection, String sUsername) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ? LIMIT 1;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sUsername);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                setUserParams(result);
            }
        }
        return true;
    }

    public void insert(Connection connection) throws Exception {

    }

    public void delete(Connection connection) throws Exception {
    }

    public void update(Connection connection) throws Exception {
    }

    @Override
    public void INSERT(DatabaseClient dbClient) throws Exception {
        try (var db = IonEnvironment.getDatabase().getClient()) {
            String sql = "INSERT INTO users" +
                    "(username,password,signedup,email,dob,motto,figure,gender,coins,films,gametickets) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
                addUserParams(stmt);
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void DELETE(DatabaseClient dbClient) throws SQLException {
        try (var db = IonEnvironment.getDatabase().getClient()) {
            String sql = "DELETE FROM users WHERE id = ? LIMIT 1;";
            try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
                stmt.setLong(1, mID);
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void UPDATE(DatabaseClient dbClient) throws Exception {
        try (var db = IonEnvironment.getDatabase().getClient()) {
            String sql = "UPDATE users " +
                    "SET username=?,password=?,signedup=?,email=?,dob=?,motto=?,figure=?,gender=?,coins=?,films=?,gametickets=? " +
                    "WHERE id = ? " +
                    "LIMIT 1;";
            try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
                addUserParams(stmt);
                stmt.setLong(13, mID);
                stmt.executeUpdate();
            }
        }
    }
}