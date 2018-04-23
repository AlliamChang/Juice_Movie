package cn.cseiii.controller;

import cn.cseiii.enums.FigureType;
import cn.cseiii.model.FilmMakerVO;
import cn.cseiii.model.MovieDetailVO;
import cn.cseiii.model.MovieMadeVO;
import cn.cseiii.model.MovieShowVO;
import cn.cseiii.service.FilmMakerService;
import cn.cseiii.service.impl.FilmMakerServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lenovo on 2017/6/7.
 */
@Controller
@RequestMapping(value = "/figure")
public class FigureController {
    private FilmMakerService filmMakerService;

    public FigureController() {
        filmMakerService = new FilmMakerServiceImpl();
    }

    @RequestMapping(value = "/j{filmMakerID}")
    public ModelAndView getFigureMessage(@PathVariable int filmMakerID) {
        FilmMakerVO filmMakerVO = filmMakerService.getFilmMakerInfo(filmMakerID);
        int productionSize = filmMakerVO.getFilmHasMade().size();
        int productionPageSize;
        if (productionSize % 4 == 0) {
            productionPageSize = productionSize / 4;
        } else {
            productionPageSize = productionSize / 4 + 1;
        }
        List<FilmMakerVO> coFilmMaker = filmMakerService.coFilmMaker(filmMakerID);
        int coFilmMakerSize = coFilmMaker.size();
        int coFilmMakerPageSize;
        if (coFilmMakerSize % 4 == 0) {
            coFilmMakerPageSize = coFilmMakerSize / 4;
        } else {
            coFilmMakerPageSize = coFilmMakerSize / 4 + 1;
        }
        FigureType[] figureTypes = filmMakerVO.getAllFigureTypes().toArray(new FigureType[filmMakerVO.getAllFigureTypes().size()]);
        MovieMadeVO[] productionArray = filmMakerVO.getFilmHasMade().values().toArray(new MovieMadeVO[filmMakerVO.getFilmHasMade().size()]);
        String[] releaseYearList = new String[productionArray.length];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        for (int i = 0; i < productionArray.length; i++) {
            if (productionArray[i].getReleased() == null) {
                releaseYearList[i] = "";
            } else {
                releaseYearList[i] = formatter.format(productionArray[i].getReleased());
            }
        }
        List<String[]> rolesInMovieList = new ArrayList<String[]>();
        for (int i = 0; i < productionArray.length; i++) {
            Set<FigureType> roleSet = productionArray[i].getAssumption();
            FigureType[] figureTypeArray = roleSet.toArray(new FigureType[roleSet.size()]);
            String[] roles = new String[figureTypeArray.length];
            for (int index = 0; index < figureTypeArray.length; index++) {
                roles[index] = figureTypeArray[index].name().toLowerCase();
            }
            rolesInMovieList.add(roles);
        }
        ModelAndView modelAndView = new ModelAndView("filmMaker");
        modelAndView.addObject("filmMakerVO", filmMakerVO);
        modelAndView.addObject("productionPageSize", productionPageSize);
        modelAndView.addObject("coFilmMaker", coFilmMaker);
        modelAndView.addObject("coFilmMakerPageSize", coFilmMakerPageSize);
        modelAndView.addObject("productionArray", productionArray);
        modelAndView.addObject("figureTypes", figureTypes);
        modelAndView.addObject("releaseYearList", releaseYearList);
        modelAndView.addObject("rolesInMovieList", rolesInMovieList);
        if (filmMakerVO.getBorn() == null || filmMakerVO.getBorn().equals("")) {
            modelAndView.addObject("born", "unknown");
        } else {
            modelAndView.addObject("born", filmMakerVO.getBorn());
        }
        if (filmMakerVO.getDeath() == null || filmMakerVO.getDeath().equals("")) {
            modelAndView.addObject("death", "until now");
        } else {
            modelAndView.addObject("death", filmMakerVO.getDeath());
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/getProduction", method = RequestMethod.GET)
    public JSONArray getProductionPage(HttpServletRequest request) {
        int figureID = Integer.parseInt(request.getParameter("figureID"));
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        JSONArray productionPageJson = new JSONArray();
        Map<Integer, MovieMadeVO> productionSet = filmMakerService.getFilmMakerInfo(figureID).getFilmHasMade();
        MovieMadeVO[] productionList = productionSet.values().toArray(new MovieMadeVO[productionSet.size()]);
        List<MovieMadeVO> productionPage = new ArrayList<MovieMadeVO>();
        int desIndex = Math.min(pageIndex * 4 + 4, productionList.length);
        for (int index = pageIndex * 4; index < desIndex; index++) {
            JSONObject productionJson = new JSONObject();
            productionJson.put("productionPiece", productionList[index]);
            Set<FigureType> roleSet = productionList[index].getAssumption();
            FigureType[] figureTypeArray = roleSet.toArray(new FigureType[roleSet.size()]);
            String[] roles = new String[figureTypeArray.length];
            for (int i = 0; i < figureTypeArray.length; i++) {
                roles[i] = figureTypeArray[i].name().toLowerCase();
            }
            productionJson.put("role", roles);
            SimpleDateFormat format=new SimpleDateFormat("yyyy");
            productionJson.put("year", format.format(productionList[index].getReleased()));
            productionPageJson.add(productionJson);
        }
        return productionPageJson;
    }

    @ResponseBody
    @RequestMapping(value = "/getCoWorkers", method = RequestMethod.GET)
    public List<FilmMakerVO> getCoWorkersPage(HttpServletRequest request) {
        int figureID = Integer.parseInt(request.getParameter("figureID"));
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        List<FilmMakerVO> coWorkerList = filmMakerService.coFilmMaker(figureID);
        List<FilmMakerVO> coWorkerPage = new ArrayList<FilmMakerVO>();
        int desIndex = Math.min(pageIndex * 4 + 4, coWorkerList.size());
        for (int index = pageIndex * 4; index < desIndex; index++) {
            coWorkerPage.add(coWorkerList.get(index));
        }
        return coWorkerPage;
    }
}
