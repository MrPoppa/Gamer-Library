package com.benji.controllers;

import com.benji.ejb.GameBrandFacade;
import com.benji.ejb.GameFacade;
import com.benji.ejb.GameOfTheDayFacade;
import com.benji.ejb.GameRatingFacade;
import com.benji.ejb.GenreFacade;
import com.benji.ejb.OwnerFacade;
import com.benji.entities.Game;
import com.benji.entities.GameBrand;
import com.benji.entities.GameOfTheDay;
import com.benji.entities.GameRating;
import com.benji.entities.Genre;
import com.benji.exceptions.DataNotFoundException;
import java.util.ArrayList;
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
    @EJB
    GameRatingFacade gameRatingFacade;
    @EJB
    GenreFacade genreFacade;
    @EJB
    GameBrandFacade gameBrandFacade;

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
            operationName = "createGame",
            action = "create_game"
    )
    public Game createGame(
            @WebParam(partName = "gameName") String gameName,
            @WebParam(partName = "ratingId") Integer ratingId,
            @WebParam(partName = "brandId") Integer brandId,
            @WebParam(partName = "genreIds") List<Integer> genreIds
    ) {
        GameBrand gameBrand = gameBrandFacade.find(brandId);
        GameRating gameRating = gameRatingFacade.find(ratingId);

        Game game = new Game();
        game.setGameName(gameName);
        game.setRating(gameRating);
        game.setBrand(gameBrand);
        gameFacade.create(game);
        for (Integer genreId : genreIds) {
            Genre genre = genreFacade.find(genreId);
            if (genre == null) {
                throw new DataNotFoundException("Genre with id " + genreId + " does not exist.");
            } else {
                game.getGenreList().add(genre);
                genre.getGameList().add(game);
                genreFacade.edit(genre);
            }
        }
        gameFacade.edit(game);
        return game;
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
