package be.skdebrug.website.controller;

import be.skdebrug.website.endpoint.SQLiteConnection;
import be.skdebrug.website.repository.GameRepository;
import be.skdebrug.website.repository.NewsRepository;
import be.skdebrug.website.repository.PlayerRepository;
import be.skdebrug.website.repository.TeamRepository;
import be.skdebrug.website.service.GameService;
import be.skdebrug.website.service.NewsService;
import be.skdebrug.website.service.PlayerService;
import be.skdebrug.website.service.TeamService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Developer: Ben Oeyen
 * Date: 07/08/15
 */
public class DevControllerTest {

    private DevController devController;
    private GameController gameController;
    private NewsController newsController;
    private TeamController teamController;
    private PlayerController playerController;

    @Before
    public void before(){
        SQLiteConnection.databaseLocation = "test.db";
        GameRepository.dropDatabaseOnInjection = false;
        NewsRepository.dropDatabaseOnInjection = false;
        PlayerRepository.dropDatabaseOnInjection = false;
        TeamRepository.dropDatabaseOnInjection = false;
        Injector injector = Guice.createInjector();
        devController = new DevController();
        gameController = new GameController();
        newsController = new NewsController();
        teamController = new TeamController();
        playerController = new PlayerController();
        injector.injectMembers(devController);
        injector.injectMembers(gameController);
        injector.injectMembers(newsController);
        injector.injectMembers(teamController);
        injector.injectMembers(playerController);
    }

    @Test
    public void FillAndClear(){
        assertThat(gameController.getAll().size()).isEqualTo(0);
        assertThat(playerController.getAll().size()).isEqualTo(0);
        assertThat(newsController.getAll().size()).isEqualTo(0);
        assertThat(teamController.getAll().size()).isEqualTo(0);
        devController.fill();
        assertThat(gameController.getAll().size()).isEqualTo(132);
        assertThat(playerController.getAll().size()).isEqualTo(19);
        assertThat(newsController.getAll().size()).isEqualTo(3);
        assertThat(teamController.getAll().size()).isEqualTo(12);
        devController.clear();
        assertThat(gameController.getAll().size()).isEqualTo(0);
        assertThat(playerController.getAll().size()).isEqualTo(0);
        assertThat(newsController.getAll().size()).isEqualTo(0);
        assertThat(teamController.getAll().size()).isEqualTo(0);
    }
}
