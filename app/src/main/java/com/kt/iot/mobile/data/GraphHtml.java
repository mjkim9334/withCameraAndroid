package com.kt.iot.mobile.data;

import com.kt.gigaiot_sdk.data.Log;
import com.kt.iot.mobile.utils.Util;

import java.util.ArrayList;

/**
 * Created by ceoko on 15. 5. 21..
 */
public class GraphHtml {

        public static final String TAG = GraphHtml.class.getSimpleName();

     public static String getGraphHtmlString(ArrayList<LogStream> data){

             String graphHtml = HEADER;
             String scriptPart = BODY_SCRIPT;

             String divPart = "";
             String scriptPartIn = "";

             for(int i=0; i<data.size(); i++){

                     LogStream logStream = data.get(i);
                     ArrayList<Log> logs = logStream.getLogList();
                     String tagId = logStream.getTag().getTagStrmId();

                     divPart += String.format(BODY_DIV, tagId.toUpperCase(), tagId.toUpperCase());
                     String valuePart = "";
                     String occDtPart = "";
                  //   String ex_valuePart = "";
                     String ex_occDtPart = "";
                 if(logs != null && logs.size() > 0) {
                     for (int j = logs.size()-1; j >=0; j--) {

                                     if(!ex_occDtPart.equals( Util.timeFormatToSimple(logs.get(j).getOccDt()))&&((ex_occDtPart).compareTo(Util.timeFormatToSimple(logs.get(j).getOccDt()))<0)) {
                                          //  if(!logs.get(j).getAttributes().get(tagId).equals("")) {
                                                     android.util.Log.w(TAG, "!!!!!!!!" + logs.get(j).getOccDt());
                                                     valuePart += logs.get(j).getAttributes().get(tagId);
                                                     valuePart += ",";

                                                     occDtPart += "\"";
                                                     occDtPart += Util.timeFormatToSimple(logs.get(j).getOccDt());
                                                     occDtPart += "\"";
                                                     occDtPart += ",";

                                                     ex_occDtPart = Util.timeFormatToSimple(logs.get(j).getOccDt());

                                          //   }
                                     }
                     }
                              valuePart = valuePart.substring(0, valuePart.length()-1);
                             occDtPart = occDtPart.substring(0, occDtPart.length()-1);
                     }
                 else{

                             valuePart = "\"\",\"\",\"\",\"\",\"\"";
                     }

                     android.util.Log.w(TAG, "getGraphHtmlString " + tagId.toUpperCase() + " valuePart = " + valuePart + " occDtPart = " + occDtPart);

                     scriptPartIn += String.format(BODY_SCRIPT_IN, i, tagId.toUpperCase(), i, occDtPart,valuePart);

             }

             if(divPart.equals("")){
                     //모든 계측 태그가 데이터가 없는 경우 null 반환.
                     return null;
             }

             graphHtml = graphHtml + divPart + String.format(scriptPart, scriptPartIn) + FOOTER;


             return graphHtml;
     }


    public static final String HEADER =
            "<!doctype html>" +
            "<html>" +
            "<head>" +
            "<title>Line Chart with Custom Tooltips</title>" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
            "<script src=\"Chart.js\">" +
            "</script>" +
            "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js\">" +
            "</script>" +
            "<style>" +
                    "#canvas-holder1 {" +
                    "width: 300px;" +
                    "margin: 20px auto;" +
                    "}" +
                    "#canvas-holder2 {" +
                    "width: 80%;" +
                    "margin: 20px 10%;" +
                    "}" +
                    "#chartjs-tooltip {" +
                    "opacity: 1;" +
                    "position: absolute;" +
                    "background: rgba(0, 0, 0, .7);" +
                    "color: white;" +
                    "padding: 3px;" +
                    "border-radius: 3px;" +
                    "-webkit-transition: all .1s ease;" +
                    "transition: all .1s ease;" +
                    "pointer-events: none;" +
                    "-webkit-transform: translate(-50%, 0);" +
                    "transform: translate(-50%, 0);" +
                    "}" +
                    ".chartjs-tooltip-key{" +
                    "display:inline-block;" +
                    "width:10px;" +
                    "height:10px;" +
                    "}" +
            "</style>" +
            "</head>" +
            "<body>";


    public static final String BODY_DIV = "<div id=\"canvas-holder2\">" +
            "<b>%s</b>" +
            "<canvas id=\"%s\" width=\"400\" height=\"300\" />" +
            "</div>";

        public static final String BODY_SCRIPT = "<script>" +
                "window.onload = function() {%s};";

        public static final String BODY_SCRIPT_IN =
                "var ctx%s = document.getElementById(\"%s\").getContext(\"2d\");" +
                        "window.myLine = new Chart(ctx%s).Line({" +
                        "labels: [%s]," +
                        "datasets: [{" +
                        "label: \"My First dataset\"," +
                        "fillColor: \"rgba(220,220,220,0.2)\"," +
                        "strokeColor: \"rgba(220,220,220,1)\"," +
                        "pointColor: \"rgba(220,220,220,1)\"," +
                        "pointStrokeColor: \"#fff\"," +
                        "pointHighlightFill: \"#fff\"," +
                        "pointHighlightStroke: \"rgba(220,220,220,1)\"," +
                        "data: [%s]" +
                        "}]" +
                        "}, {" +
                        "responsive: true" +
                        "});";


    public static final String FOOTER =
            "Chart.defaults.global.animation = false;" +
            "Chart.defaults.global.pointHitDetectionRadius = 1;" +
            "Chart.defaults.global.customTooltips = function(tooltip) {" +
            "var tooltipEl = $('#chartjs-tooltip');" +
            "if (!tooltip) {" +
            "tooltipEl.css({" +
            "opacity: 0" +
            "});" +
            "return;" +
            "}" +
            "tooltipEl.removeClass('above below');" +
            "tooltipEl.addClass(tooltip.yAlign);" +
            "var innerHtml = '';" +
            "for (var i = tooltip.labels.length - 1; i >= 0; i--) {" +
            "innerHtml += [" +
            "'<div class=\"chartjs-tooltip-section\">'," +
            "'\t<span class=\"chartjs-tooltip-key\" style=\"background-color:' + tooltip.legendColors[i].fill + '\"></span>'," +
            "'\t<span class=\"chartjs-tooltip-value\">' + tooltip.labels[i] + '</span>'," +
            "'</div>'" +
            "].join('');" +
            "}" +
            "tooltipEl.html(innerHtml);" +
            "tooltipEl.css({" +
            "opacity: 1," +
            "left: tooltip.chart.canvas.offsetLeft + tooltip.x + 'px'," +
            "top: tooltip.chart.canvas.offsetTop + tooltip.y + 'px'," +
            "fontFamily: tooltip.fontFamily," +
            "fontSize: tooltip.fontSize," +
            "fontStyle: tooltip.fontStyle," +
            "});" +
            "};" +
            "</script>" +
            "</body>" +
            "</html>";

}
