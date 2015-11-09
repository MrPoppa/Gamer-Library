package com.benji.controllers;

import com.benji.ejb.GameFacade;
import com.benji.ejb.GameOfTheDayFacade;
import com.benji.ejb.OwnerFacade;
import com.benji.entities.Game;
import com.benji.entities.GameOfTheDay;
import com.benji.exceptions.DataNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Benjamin Bengtsson
 */
@WebService(
        name = "GameController",
        serviceName = "GameService",
        portName = "GamePort"
)
public class SOAPGameController {

    @EJB
    GameFacade gameFacade;
    @EJB
    GameOfTheDayFacade gotd;
    @EJB
    OwnerFacade ownerFacade;

    @WebMethod(
            operationName = "getGameById",
            action = "get_game_by_id"
    )
    public Game getGameById(
            @WebParam(partName = "gameId") int gameId
    ) {
        Game game = gameFacade.find(gameId);
        if (game == null) {
            throw new DataNotFoundException("Game with id " + gameId + " does not exist.");
        } else {
            return game;
        }
    }

    @WebMethod(
            operationName = "getAllGames",
            action = "get_all_games"
    )
    public List<Game> getAllGames() {
        List<Game> games = gameFacade.findAll();
        return games;
    }

    @WebMethod(
            operationName = "getGameOfTheDay",
            action = "get_game_of_the_day"
    )
    public Game getGameOfTheDay() {
        GameOfTheDay gameOfTheDay = gotd.find(1);
        if (gameOfTheDay == null) {
            gameOfTheDay = new GameOfTheDay();
            List<Game> allGames = gameFacade.findAll();
            Random rnd = new Random();
            gameOfTheDay.setId(1);
            gameOfTheDay.setGame(allGames.get(rnd.nextInt(allGames.size())));
            gameOfTheDay.setLastUpdateDate(new Date(System.currentTimeMillis()));
            gotd.create(gameOfTheDay);
        } else {
            long lastUpdate = gameOfTheDay.getLastUpdateDate().getTime();
            long today = new Date(System.currentTimeMillis()).getTime();

            if (today - lastUpdate >= 24 * 60 * 60 * 1000) {
                List<Game> allGames = gameFacade.findAll();
                Random rnd = new Random();
                gameOfTheDay.setGame(allGames.get(rnd.nextInt(allGames.size())));
                gameOfTheDay.setLastUpdateDate(new Date(System.currentTimeMillis()));
                gotd.edit(gameOfTheDay);
            }
        }
        return gameOfTheDay.getGame();
    }

}
