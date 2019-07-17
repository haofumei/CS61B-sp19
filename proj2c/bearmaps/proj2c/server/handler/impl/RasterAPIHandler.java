package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import bearmaps.proj2c.utils.Constants;
import spark.Request;
import spark.Response;
import static bearmaps.proj2c.utils.Constants.*;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {

        double requestUllon = requestParams.get("ullon");
        double requestUllat = requestParams.get("ullat");
        double requestLrlon = requestParams.get("lrlon");
        double requestLrlat = requestParams.get("lrlat");
        if (!isSuccessful(requestUllon, requestUllat, requestLrlon, requestLrlat)) {
            return queryFail();
        }

        Map<String, Object> results = new HashMap<>();
        double londpp = LonDPP(requestUllon, requestLrlon,requestParams.get("w"));
        imageBounds image = new imageBounds(depth(londpp), requestUllon, requestUllat, requestLrlon, requestLrlat);
        results.put("render_grid", image.tiles());
        results.put("raster_ul_lon", image.ullon);
        results.put("raster_ul_lat", image.ullat);
        results.put("raster_lr_lon", image.lrlon);
        results.put("raster_lr_lat", image.lrlat);
        results.put("depth", image.depth);
        results.put("query_success", true);
        return results;
    }

    private double LonDPP(double ullon, double lrlon, double w) {
        return (lrlon - ullon) / w;
    }

    private int depth(double LonDPP) {
        int depth = 0;
        while (depth < 7 && resolution(depth) > LonDPP) {
            depth += 1;
        }
        return depth;
    }

    private double resolution(int depth) {
        double basic = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        return basic / Math.pow(2, depth);
    }

    private boolean isSuccessful(double ullon, double ullat, double lrlon, double lrlat) {
        if (ullon >= ROOT_LRLON || ullat <= ROOT_LRLAT || lrlon <= ROOT_ULLON || lrlat >= ROOT_ULLAT) {
            return false;
        } else {
            return true;
        }
    }

    private class imageBounds {
        double ullon, lrlon, ullat, lrlat;
        double xDist, yDist;
        int depth;
        int ulX = 0;
        int ulY = 0;
        int lrX = 0;
        int lrY = 0;

        public imageBounds(int depth, double ullon, double ullat, double lrlon, double lrlat) {
            this.xDist = (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2, depth));
            this.yDist = (ROOT_ULLAT - ROOT_LRLAT) / (Math.pow(2, depth));
            this.ullon = ROOT_ULLON;
            this.lrlon = this.ullon + xDist;
            this.ullat = ROOT_ULLAT;
            this.lrlat = this.ullat - yDist;
            this.depth = depth;

            calUL(ullon, ullat);
            calLR(lrlon, lrlat);
        }

        public void calUL(double requestUllon, double requestUllat) {
            double distLon = requestUllon - this.ullon;
            double distLat = this.ullat - requestUllat;
            if (distLon < 0) {
                ulX = 0;
            } else {
                ulX = (int)(distLon / xDist);
            }
            if (distLat < 0) {
                ulY = 0;
            } else {
                ulY = (int)(distLat / yDist);
            }

            this.ullon = this.ullon + ulX * xDist;
            this.ullat = this.ullat - ulY * yDist;
        }

        public void calLR(double requestLrlon, double requestLrlat) {
            double distLon = requestLrlon - this.lrlon;
            if (distLon < 0) {
                lrX = 1;
            } else if (requestLrlon > ROOT_LRLON){
                lrX = (int)Math.pow(2, depth) -1;
            } else {
                lrX = (int)(distLon / xDist) + 1;
            }
            if (distLon % xDist == 0) {
                lrX = lrX - 1;
            }

            double distLat = this.lrlat - requestLrlat;
            if (distLat < 0) {
                lrY = 1;
            } else if (requestLrlat < ROOT_LRLAT) {
                lrY = (int)Math.pow(2, depth) -1;
            } else {
                lrY = (int)(distLat / yDist) + 1;
            }
            if (distLat % yDist == 0) {
                lrY = lrY - 1;
            }

            this.lrlon = this.lrlon + lrX * xDist;
            this.lrlat = this.lrlat - lrY * yDist;
        }

        public String[][] tiles() {
            int countX = lrX - ulX + 1;
            int countY = lrY - ulY + 1;
            String[][] tiles = new String[countY][countX];
            for (int y = 0; y < countY; y ++) {
                for (int x = 0; x < countX; x ++) {
                    tiles[y][x] = "d" + depth + "_x" + (x + ulX) + "_y" + (y + ulY) + ".png";
                }
            }
            return tiles;
        }
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
