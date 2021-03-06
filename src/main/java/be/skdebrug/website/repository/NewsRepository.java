package be.skdebrug.website.repository;

import be.skdebrug.website.core.News;
import be.skdebrug.website.endpoint.SQLiteConnection;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Developer: Ben Oeyen
 * Date: 24/05/2015
 */
public class NewsRepository extends AbstractRepository {
    private static final String TBL_NEWS = "tbl_news";
    private static final String COL_NEWS_ID = "newsID";
    private static final String COL_NEWS_DATE = "date";
    private static final String COL_NEWS_TITLE = "title";
    private static final String COL_NEWS_CONTENT = "content";

    private News parseNewsFromResult(ResultSet queryResult) throws SQLException{
        News news = new News();
        news.setId(queryResult.getInt(COL_NEWS_ID));
        news.setDate(new DateTime(queryResult.getLong(COL_NEWS_DATE)));
        news.setTitle(queryResult.getString(COL_NEWS_TITLE));
        news.setContent(queryResult.getString(COL_NEWS_CONTENT));
        return news;
    }

    public NewsRepository() {
        if (dropDatabaseOnInjection) {
            (new SQLiteConnection<Boolean>() {
                @Override
                public Boolean defineOperation(Statement statement) throws SQLException {
                    statement.execute(log("DROP TABLE IF EXISTS " + TBL_NEWS));
                    return true;
                }
            }).runOperation();
        }
        (new SQLiteConnection<Boolean>() {
            @Override
            public Boolean defineOperation(Statement statement) throws SQLException {
                statement.execute(log("CREATE TABLE IF NOT EXISTS " + TBL_NEWS + " ("
                        + COL_NEWS_ID + " INTEGER PRIMARY KEY ASC,"
                        + COL_NEWS_DATE + ","
                        + COL_NEWS_TITLE + ","
                        + COL_NEWS_CONTENT + ","
                        + "CONSTRAINT unique_" + TBL_NEWS + " UNIQUE(" + COL_NEWS_ID + ") ON CONFLICT IGNORE );"));
                return true;
            }
        }).runOperation();
    }

    public boolean create(final News news) {
        return (new SQLiteConnection<Boolean>() {
            @Override
            public Boolean defineOperation(Statement statement) throws SQLException {
                statement.executeUpdate(log("INSERT INTO " + TBL_NEWS + " ("
                        + COL_NEWS_DATE + ","
                        + COL_NEWS_TITLE + ","
                        + COL_NEWS_CONTENT +  ") "
                        + "VALUES ('"
                        + news.getDate().getMillis() + "','"
                        + escapeSingleQuotes(news.getTitle()) + "','"
                        + escapeSingleQuotes(news.getContent()) + "');"));
                return true;
            }
        }).runOperation();
    }

    public News get(final int newsId) {
        return (new SQLiteConnection<News>() {
            @Override
            public News defineOperation(Statement statement) throws SQLException {
                ResultSet queryResult = statement.executeQuery(log("SELECT * FROM " + TBL_NEWS + " WHERE " + COL_NEWS_ID + " = '" + newsId + "'"));
                if (queryResult.next()) {
                    return parseNewsFromResult(queryResult);
                }
                return null;
            }
        }).runOperation();
    }

    public List<News> getAll() {
        return (new SQLiteConnection<List<News>>() {
            @Override
            public List<News> defineOperation(Statement statement) throws SQLException {
                List<News> newsList = new ArrayList<>();
                ResultSet queryResult = statement.executeQuery(log("SELECT * FROM " + TBL_NEWS + " ORDER BY " + COL_NEWS_DATE + " DESC"));
                while (queryResult.next()) {
                    newsList.add(parseNewsFromResult(queryResult));
                }
                return newsList;
            }
        }).runOperation();
    }

    public boolean update(final News news) {
        return (new SQLiteConnection<Boolean>() {
            @Override
            public Boolean defineOperation(Statement statement) throws SQLException {
                statement.executeUpdate(log("UPDATE " + TBL_NEWS
                        + " SET "
                        + COL_NEWS_DATE + " = '" + news.getDate().getMillis() + "', "
                        + COL_NEWS_TITLE + " = '" + escapeSingleQuotes(news.getTitle()) + "', "
                        + COL_NEWS_CONTENT + " = '" + escapeSingleQuotes(news.getContent()) + "' "
                        + "WHERE " + COL_NEWS_ID + " = '" + news.getId() + "' "));
                return true;
            }
        }).runOperation();
    }

    public boolean delete(final int newsId) {
        return (new SQLiteConnection<Boolean>() {
            @Override
            public Boolean defineOperation(Statement statement) throws SQLException {
                statement.execute(log("DELETE FROM " + TBL_NEWS + " WHERE " + COL_NEWS_ID + " = '" + newsId + "'"));
                return true;
            }
        }).runOperation();
    }

    public boolean deleteAll() {
        return (new SQLiteConnection<Boolean>() {
            @Override
            public Boolean defineOperation(Statement statement) throws SQLException {
                statement.execute(log("DELETE FROM " + TBL_NEWS ));
                return true;
            }
        }).runOperation();
    }
}