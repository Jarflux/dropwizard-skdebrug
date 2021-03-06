package be.skdebrug.website.repository;

import be.skdebrug.website.core.News;
import be.skdebrug.website.core.Team;
import be.skdebrug.website.endpoint.SQLiteConnection;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Developer: Ben Oeyen
 * Date: 23/07/15
 */
public class TeamRepositoryTest {

    private TeamRepository teamRepository;

    @Before
    public void before() {
        SQLiteConnection.databaseLocation = "test.db";
        TeamRepository.dropDatabaseOnInjection = true;
        teamRepository = new TeamRepository();
    }

    @Test
    public void testCreateAndGet(){
        Team teamBefore = new Team();
        teamBefore.setName("Anderlecht");
        teamRepository.create(teamBefore);
        Team teamAfter = teamRepository.getAll().get(0);
        assertThat(teamAfter.getId()).isNotNull();
        assertThat(teamAfter.getName()).isEqualTo(teamBefore.getName());
    }

    @Test
    public void testGetSpecificAndDelete(){
        Team teamBefore = new Team();
        teamBefore.setName("Anderlecht");
        teamRepository.create(teamBefore);
        teamBefore =  teamRepository.getAll().get(0);
        int teamId = teamBefore.getId();
        Team teamAfter = teamRepository.get(teamId);
        assertThat(teamAfter.getId()).isEqualTo(teamBefore.getId());
        assertThat(teamAfter.getName()).isEqualTo(teamBefore.getName());
        teamRepository.delete(teamId);
        assertThat(teamRepository.getAll().size()).isEqualTo(0);
    }

    @Test
    public void testInsertWithSingleQuote(){
        Team teamBefore = new Team();
        teamBefore.setName("Anderle'cht");
        teamRepository.create(teamBefore);
        Team teamAfter = teamRepository.getAll().get(0);
        assertThat(teamAfter.getId()).isNotNull();
        assertThat(teamAfter.getName()).isEqualTo(teamBefore.getName());
    }

    @Test
    public void testCreateAndUpdate(){
        Team teamBefore = new Team();
        teamBefore.setName("Anderlecht");
        teamRepository.create(teamBefore);

        teamBefore = teamRepository.getAll().get(0);
        teamBefore.setName("Anderlecht2");
        teamRepository.update(teamBefore);
        assertThat(teamRepository.getAll().size()).isEqualTo(1);
        Team teamAfter = teamRepository.getAll().get(0);
        assertThat(teamAfter.getId()).isNotNull();
        assertThat(teamAfter.getName()).isEqualTo(teamBefore.getName());
    }

    @Test
    public void testCreateMultipleAndDeleteAll(){
        Team teamBefore = new Team();
        teamBefore.setName("Anderlecht");
        teamRepository.create(teamBefore);
        teamRepository.create(teamBefore);
        assertThat(teamRepository.getAll().size()).isEqualTo(2);
        teamRepository.deleteAll();
        assertThat(teamRepository.getAll().size()).isEqualTo(0);
    }

    @Test
    public void testGetTeamWithUnexistingId(){
        assertThat(teamRepository.get(10000)).isEqualTo(null);
    }

    @Test
    public void testGetTeamWithUnexistingName(){
        assertThat(teamRepository.get("qweriiudsdfghjhb")).isEqualTo(null);
    }

}
