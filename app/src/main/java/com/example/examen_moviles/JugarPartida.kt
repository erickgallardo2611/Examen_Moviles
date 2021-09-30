package com.example.examen_moviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.webkit.WebView
import android.widget.TextView

class JugarPartida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jugar_partida)
        var nombre = intent.getStringExtra("nombre").toString()
        var elo = intent.getStringExtra("elo").toString()
        var nombreJugador = findViewById<TextView>(R.id.txtNombreJugador)
        var eloJugador = findViewById<TextView>(R.id.txtEloJugador)
        var viewTablero = findViewById<WebView>(R.id.webVierwTablero)
        nombreJugador.setText(nombre)
        eloJugador.setText(elo)

        val unencodedHtml =
            "<table width=\"400\" height=\"400px\"; border=\"0\" cellspacing=\"2\" cellpadding=\"2\" bgcolor=\"#000000\">\n" +
                    "<tr align=\"center\">\n" +
                    "<td><font color=\"#ffffff\">1</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">2</td>\n" +
                    "<td><font color=\"#ffffff\">3</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">4</td>\n" +
                    "<td><font color=\"#ffffff\">5</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">6</td>\n" +
                    "<td><font color=\"#ffffff\">7</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">8</td>\n" +
                    "</tr>\n" +
                    "<tr align=\"center\">\n" +
                    "<td bgcolor=\"#ffffff\">9</td>\n" +
                    "<td><font color=\"#ffffff\">10</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">11</td>\n" +
                    "<td><font color=\"#ffffff\">12</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">13</td>\n" +
                    "<td><font color=\"#ffffff\">14</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">15</td>\n" +
                    "<td><font color=\"#ffffff\">16</font></td>\n" +
                    "</tr>\n" +
                    "<tr align=\"center\">\n" +
                    "<td><font color=\"#ffffff\">17</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">18</td>\n" +
                    "<td><font color=\"#ffffff\">19</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">20</td>\n" +
                    "<td><font color=\"#ffffff\">21</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">22</td>\n" +
                    "<td><font color=\"#ffffff\">23</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">24</td>\n" +
                    "</tr>\n" +
                    "<tr align=\"center\">\n" +
                    "<td bgcolor=\"#ffffff\">25</td>\n" +
                    "<td><font color=\"#ffffff\">26</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">27</td>\n" +
                    "<td><font color=\"#ffffff\">28</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">29</td>\n" +
                    "<td><font color=\"#ffffff\">30</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">31</td>\n" +
                    "<td><font color=\"#ffffff\">32</font></td>\n" +
                    "</tr>  <tr align=\"center\">\n" +
                    "<td><font color=\"#ffffff\">33</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">34</td>\n" +
                    "<td><font color=\"#ffffff\">35</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">36</td>\n" +
                    "<td><font color=\"#ffffff\">37</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">38</td>\n" +
                    "<td><font color=\"#ffffff\">39</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">40</td>\n" +
                    "</tr>\n" +
                    "<tr align=\"center\">\n" +
                    "<td bgcolor=\"#ffffff\">41</td>\n" +
                    "<td><font color=\"#ffffff\">42</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">43</td>\n" +
                    "<td><font color=\"#ffffff\">44</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">45</td>\n" +
                    "<td><font color=\"#ffffff\">46</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">47</td>\n" +
                    "<td><font color=\"#ffffff\">48</font></td>\n" +
                    "</tr>  <tr align=\"center\">\n" +
                    "<td><font color=\"#ffffff\">49</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">50</td>\n" +
                    "<td><font color=\"#ffffff\">51</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">52</td>\n" +
                    "<td><font color=\"#ffffff\">53</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">54</td>\n" +
                    "<td><font color=\"#ffffff\">55</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">56</td>\n" +
                    "</tr>  <tr align=\"center\">\n" +
                    "<td bgcolor=\"#ffffff\">57</td>\n" +
                    "<td><font color=\"#ffffff\">58</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">59</td>\n" +
                    "<td><font color=\"#ffffff\">60</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">61</td>\n" +
                    "<td><font color=\"#ffffff\">62</font></td>\n" +
                    "<td bgcolor=\"#ffffff\">63</td>\n" +
                    "<td><font color=\"#ffffff\">64</font></td>\n" +
                    "</tr>\n" +
                    "</table>";
        val encodedHtml = Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
        viewTablero.loadData(encodedHtml, "text/html", "base64")

    }
}