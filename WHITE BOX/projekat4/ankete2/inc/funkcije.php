<?php
$pop=0;
function greska($g)
{
    echo "<script>greska(" . json_encode($g) . ")</script>;";
}
if(isset($_GET['pop']) && $_GET['pop']==1 ){$pop=1;}

function rasporedi(){}

?>
<?php
function prikaziTabelu($qr)
{
if ($qr->num_rows > 0) {
?>
<tbody id="tbody">
<?php
while ($n = $qr->fetch_assoc()) {
    $qq1="SELECT * FROM ispitanik_anketa WHERE idAnketa='".$n['idAnketa']."' AND username='".$_SESSION['kime']."';";
    $qr1=qq($qq1);
    $pop=0;
    if($qr1->num_rows>0){
        $nn=$qr1->fetch_assoc();
        if($nn['popunjena']==1){$pop=1;}
    }
    ?>

    <tr>
        <td><a class="linkdugme" ida='<?php echo $n['idAnketa']; ?>'><?php echo $n['naziv']; ?></a></td>
        <td><?php echo $n['datum_pocetka']; ?></td>
        <td><?php echo $n['datum_kraja']; ?></td>
        <?php if ($pop != 0) { ?>
            <td><i class="icon checkmark"></i> Popunjena</td>
            <?php
        } else {
            ?>
            <td class="positive"><i class="icon close"></i> Nije popunjena</td>
            <?php
        }
        ?>
        <td><?php echo $n['autor']; ?></td>
    </tr>
    <?php
}
?>


<?php
}
}
function header_tabele()
{ ?>
    <label for="sort1" class="labelsel">Sortiraj:</label>
    <select class="ui dropdown" id="sort1">
        <option value="-1"></option>
        <option value="1">Rastuce</option>
        <option value="0">Opadajuce</option>
    </select>

    <label for="sort2">Kriterijum:</label>
    <select class="ui dropdown" id="sort2">
        <option value="-1"></option>
        <option value="1">Naziv</option>
        <option value="2">Datum pocetka</option>
        <option value="3">Datum kraja</option>
    </select>
    <br>
    <br>
    <br>
    <br>

    <table class="ui celled sortable table tabelaAnketa" id="tabela">
        <thead>
        <tr>
            <th>Anketa</th>
            <th>Datum pocetka</th>
            <th>Datum kraja</th>
            <th>Popunjena</th>
            <th>Autor</th>
        </tr>
        </thead>
        <tbody>
        <?php
        $q = "SELECT * FROM anketa WHERE blokirana=0";
        $qr = qq($q);
        //print_r($qr->fetch_all(MYSQLI_ASSOC));

        prikaziTabelu($qr);


        ?>
        </tbody>
    </table>
<?php }


function prikazi_anketu($idA){

    $q = "SELECT * FROM anketa WHERE idAnketa='" . $idA . "';";
    $qr = qq($q);
    if ($qr->num_rows == 1) {
        $niz = $qr->fetch_assoc();
        $naziv = $niz['naziv'];
        $dp = $niz['datum_pocetka'];
        $dk = $niz['datum_kraja'];
        $brstr = $niz['broj_strana'];
        $qq1 = "SELECT * FROM ispitanik_anketa WHERE idAnketa='" . $idA . "' AND username='" . $_SESSION['kime'] . "';";
        $qr1 = qq($qq1);

        if ($qr1->num_rows > 0) {
            $nn = $qr1->fetch_assoc();
            if ($nn['popunjena'] == 1) {
                $pop = 1;
            }
        }
//        $pop = $niz['popunjena'];
        if (date('Y-m-d') > $dk) { echo "<h3>Istekla anketa!</h3>";exit();
        } else {
            ?>
            <h1 class="ui header centar"><?php echo $naziv; ?></h1>

        <?php

        $q = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja 
                ON anketa_pitanja.idPitanja =pitanja.idPitanja
                WHERE anketa_pitanja.idAnketa='" . $idA . "' ORDER BY red;";

        $qr = qq($q);
        $r = qq($q);
        $r = $r->fetch_all(MYSQLI_ASSOC);
        $r = json_encode($r);

        if ($qr->num_rows > 0) {
        $i = 0; ?>

            <script>
                anketa =<?php echo $idA;?>;
                niz =<?php echo $r;?>;
                pitanja = [];
                var i = 0;
                while (p = niz[i]) {
                    var pit = {
                        "idP": p['idPitanja'],
                        "tp": p['tipOdgovora'],
                        "tekst_p": p['tekst'],
                        "ob": p['obavezno'],
                        "pp": []
                    };
                    pitanja.push(pit);
                    i++;
                }
                // console.log(pitanja);

            </script>

            <form class="ui form " id="anketa" name="anketa" action="">
                <!--            -->
                <?php
                $pitstr = $qr->num_rows / $brstr;
                $br_pit = $qr->num_rows;
                $pon = true;
                while ($n = $qr->fetch_assoc()) {
                    $i += 1;
                    prikazi_pitanje($n, $i);
                } ?>

                <?php

                ?>
                <input class="ui right floated button" id="sledeca" value="Sledeca" type="button"
                       onclick="menjaj_str(true)">
                <input class="ui left floated button" id="prethodna" value="Prethodna" type="button"
                       onclick="menjaj_str(false)">
                <br><br><br>
                <input type="hidden" name="ida" value="<?php echo $idA; ?>">

                <input class="ui button savedugme" value="ZAPAMTI" name="zapamtiAnketa" type="button">
                <button class="ui button submitdugme" name="potvrdiAnketa" type="button" onclick="provera_anketa()">
                    POTVRDI
                </button>
            </form>
            <div class="ui error message hidden" id="greskadiv">
                <!--            <i class="close icon"></i>-->
                <div class="header" id="porukadiv">

                </div>
            </div>

            <div class="ui indicating progress" id="progressbar">
                <div class="bar">
                </div>
                <div class="label">Popunjenost ankete</div>
            </div>

            <script>
                $(document).ready(function () {
                        console.log(pop);

                        izmenjivi = $(".izmenjiv");
                        //console.log(izmenjivi);
                        if (pop == 1) {
                            document.getElementsByName('zapamtiAnketa')[0].style.display = 'none';
                            document.getElementsByName('potvrdiAnketa')[0].style.display = 'none';
                            var i = 0;
                            while (izmenjivi[i]) {
                                //console.log(izmenjivi[i]);
                                izmenjivi[i].disabled = true;
                                i++;
                            }
                        }
                        console.log('Boki');
                        //rez=null;
                        if (rez) {
                            pitanja = rez;
                            console.log("REZ", rez);
                            var i = 0;
                            var br = 0;

                            while (rez[i]) {
                                // console.log(rez[i]);
                                var j = 0;
                                if (rez[i]['tp'] === '1' || rez[i]['tp'] === '2' || rez[i]['tp'] === '3' || rez[i]['tp'] === '9') {

                                    while (rez[i]['pp'][j]) {
                                        console.log(izmenjivi[br]);
                                        izmenjivi[br].value = rez[i]['pp'][j]['o'];
                                        br++;
                                        j++;
                                    }
                                }
                                if (rez[i]['tp'] === '4') {
                                    console.log(rez[i]);
                                    while (rez[i]['pp'][j]) {
                                        if (rez[i]['pp'][j]['o'])
                                            izmenjivi[br].value = rez[i]['pp'][j]['tekst_pp'];

                                        j++;
                                    }
                                    br++;
                                }

                                if (rez[i]['tp'] === '5') {
                                    while (rez[i]['pp'][j]) {
                                        izmenjivi[br].checked = rez[i]['pp'][j]['o'];

                                        br++;
                                        j++;
                                    }
                                }
                                if (rez[i]['tp'] === '6' || rez[i]['tp'] === '7') {
                                    while (rez[i]['pp'][j]) {
                                        var j2 = 0;
                                        while (rez[i]['pp'][j]['o'][j2]) {
                                            izmenjivi[br].checked = rez[i]['pp'][j]['o'][j2]['val'];
                                            br++;
                                            j2++;
                                        }
                                        // izmenjivi[br].checked=rez[i]['pp'][j]['o'];

                                        j++;
                                    }
                                }
                                i++;
                            }

                        }
                        statistika();
                    }
                );


                function prikazi_pitanje(id, bool) {
                    var pitanje = document.getElementById('pitanje[' + id + ']');
                    if (pitanje) {
                        if (bool) {
                            pitanje.style.display = 'block';
                        } else {
                            pitanje.style.display = 'none';
                        }
                    }
                }

                trenutna_strana = 0;
                brstr =<?php echo $brstr;?>;
                br_pit =<?php echo $br_pit;?>;
                pitstr = Math.ceil(br_pit / brstr);

                console.log("pit str", pitstr);

                function menjaj_str(bool) {
                    for (i = 0; i < br_pit; i++) {
                        prikazi_pitanje(i, false);
                    }
                    if (bool === true) {
                        if (trenutna_strana + 1 > brstr) return;
                        var pocetak = trenutna_strana * pitstr;
                        console.log("poc true", pocetak);
                        for (i = pocetak; i < pocetak + pitstr; i++) {
                            prikazi_pitanje(i, true);
                        }
                        trenutna_strana = trenutna_strana + 1;
                    }
                    if (bool === false) {
                        if (trenutna_strana - 1 === 0) return;
                        var pocetak = (trenutna_strana - 2) * pitstr;
                        console.log("poc false", pocetak);
                        for (i = pocetak; i < pocetak + pitstr; i++) {
                            prikazi_pitanje(i, true);
                        }
                        trenutna_strana = trenutna_strana - 1;
                    }
                    console.log("str", trenutna_strana);

                    if (trenutna_strana <= 1) {
                        document.getElementById('prethodna').style.display = 'none';
                    } else {
                        document.getElementById('prethodna').style.display = 'block';
                    }

                    if (trenutna_strana >= brstr) {
                        document.getElementById('sledeca').style.display = 'none';
                    } else {
                        document.getElementById('sledeca').style.display = 'block';
                    }
                }


                menjaj_str(true);

                function updateJsonDB(data) {
                    var id_potp = data[0].getAttribute("ida");
                    var id_pita = data[0].getAttribute("idb");
                    var id_tip = data[0].getAttribute("idt");
                    // console.log(id_pita);
                    // console.log(id_potp);
                    // console.log(id_tip);
                    // console.log(data[0].value);
                    console.log(pitanja);
                    console.log(1);

                    if (pitanja[id_pita]['tp'] === '5') {
                        var i = 0;
                        while (pitanja[id_pita]['pp'][i]) {
                            if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                pitanja[id_pita]['pp'][i]['o'] = data[0].checked;
                                // console.log(pitanja);
                                break;
                            }
                            i++;
                        }
                    } else {
                        if (pitanja[id_pita]['tp'] === '6') {
                            var id_izb = data[0].getAttribute("idizb");
                            console.log("idIzb", id_izb);
                            console.log("idPotp", id_potp);
                            var i = 0;
                            while (pitanja[id_pita]['pp'][i]) {
                                j1 = 0;
                                if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                    while (pitanja[id_pita]['pp'][i]['o'][j1]) {
                                        // console.log("id_izb iz json",pitanja[id_pita]['pp'][i]['o'][j1]['idIzb'],"id_izb",id_izb);
                                        if (pitanja[id_pita]['pp'][i]['o'][j1]['idIzb'] === id_izb) {
                                            pitanja[id_pita]['pp'][i]['o'][j1]['val'] = data[0].checked;
                                            // alert(pitanja[id_pita]['pp'][i]['o'][j1]['val'] );
                                        } else {
                                            pitanja[id_pita]['pp'][i]['o'][j1]['val'] = false;
                                        }
                                        // console.log(pitanja[id_pita]['pp'][i]['o']);
                                        j1++;
                                    }
                                    //console.log(pitanja);
                                    // console.log(data[0]);

                                }
                                i++;
                            }
                        } else {
                            if (pitanja[id_pita]['tp'] === '7') {
                                var id_izb = data[0].getAttribute("idizb");
                                // alert(id_izb);
                                var i = 0;
                                while (pitanja[id_pita]['pp'][i]) {
                                    j1 = 0;
                                    if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                        while (pitanja[id_pita]['pp'][i]['o'][j1]) {
                                            if (pitanja[id_pita]['pp'][i]['o'][j1]['idIzb'] === id_izb) {
                                                pitanja[id_pita]['pp'][i]['o'][j1]['val'] = data[0].checked;
                                            }
                                            // console.log(pitanja[id_pita]['pp'][i]['o']);
                                            // alert( pitanja[id_pita]['pp'][i]['o'][j1]['val']);
                                            j1++;
                                        }
                                        console.log(pitanja);
                                        // console.log(data[0]);
                                    }
                                    i++;
                                }
                            } else {
                                if (pitanja[id_pita]['tp'] === '4') {
                                    var i = 0;
                                    while (pitanja[id_pita]['pp'][i]) {
                                        if (pitanja[id_pita]['pp'][i]['tekst_pp'] === data[0].value) {

                                            pitanja[id_pita]['pp'][i]['o'] = true;
                                        } else {
                                            pitanja[id_pita]['pp'][i]['o'] = false;
                                        }
                                        i++;
                                    }
                                } else {
                                    var i = 0;
                                    while (pitanja[id_pita]['pp'][i]) {
                                        if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                            pitanja[id_pita]['pp'][i]['o'] = data[0].value;
                                            console.log(pitanja);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                        // console.log(pitanja);
                    }
                }

                function statistika() {
                    i = 0;
                    odg = 0;
                    brp = pitanja.length;
                    while (pitanja[i]) {
                        // console.log("i=",i);
                        aa = true;
                        j = 0;
                        tp = pitanja[i]['tp'];
                        // console.log(pitanja[i]);
                        if (tp === '1' || tp === '2' || tp === '3') {
                            while (pitanja[i]['pp'][j]) {
                                if (pitanja[i]['pp'][j]['o'] == null || pitanja[i]['pp'][j]['o'] === '') {
                                    aa = false;
                                    break;
                                }
                                j++;
                            }
                            if (aa) {
                                odg++;
                            }
                            // console.log("aa",aa,"odg",odg);

                        } else {
                            if (tp === '4' || tp === '5') {
                                while (pitanja[i]['pp'][j]) {
                                    if (pitanja[i]['pp'][j]['o'] != null && pitanja[i]['pp'][j]['o'] !== false && pitanja[i]['pp'][j]['o'] !== '') {
                                        odg++;
                                        break;
                                    }
                                    j++;
                                }
                            }
                            // console.log(345);
                            if (tp === '6' || tp === '7') {
                                jj = 0;

                                var odg_pp = 0;
                                // console.log("tppp",tp);
                                while (pitanja[i]['pp'][j]) {
                                    console.log("BBBB", pitanja[i]['pp'][j]);
                                    odg_i = 0;
                                    jj = 0;
                                    while (pitanja[i]['pp'][j]['o'][jj]) {
                                        // console.log(pitanja[i]['pp'][j]['o'][jj]['val']);
                                        // alert(pitanja[i]['pp'][j]['o'][jj]['val']);
                                        console.log(pitanja[i]['pp'][j]['o'][jj]['val'], jj);
                                        if (pitanja[i]['pp'][j]['o'][jj]['val'] != null && pitanja[i]['pp'][j]['o'][jj]['val'] != false) {
                                            odg_i++;
                                        }
                                        // console.log((pitanja[i]['pp'][j]['o'][jj]['val']));
                                        jj++;
                                    }
                                    // alert(odg_i);
                                    if (odg_i > 0) {
                                        odg_pp++;
                                    }
                                    // console.log("heeej");
                                    j++;
                                }

                                // alert(pitanja[i]['pp'].length);
                                // console.log("OOOOOOO",odg_i);
                                // console.log("PPPPPP",pitanja[i]['pp'].length);
                                if (odg_pp >= pitanja[i]['pp'].length) {
                                    odg++;
                                }

                            }

                        }
                        i++;
                    }
                    proc = odg / brp * 100;
                    // console.log("odg",odg);
                    // console.log("brp",brp);
                    console.log("proc", proc);
                    progress(proc);
                }

                $(document).ready(function () {
                    $('.savedugme').click(function () {
                        var p = JSON.stringify(pitanja);
                        console.log(p);
                        $.get('inc/update_table.php', {pit: p, ida: anketa,proc: 0}, function (data) {
                            // console.log(data);
                        });
                    });

                });
                $("#anketa").submit(function () {
                    var p = JSON.stringify(pitanja);
                    // alert(document.getElementById('broj['+0+']').value);
                    // console.log(p);
                    $.get('inc/update_table.php', {pit: p, ida: anketa}, function (data) {
                        console.log(data);
                    });
                    // alert("Submitted");
                });

                // console.log(pitanja);
                $(document).ready(function () {
                    var question = $(".question");
                    var ql = $(".question").length;
                    //alert(ql);
                    var br_rb = 0;
                    var proc = 0;
                    $(".question").change(function () {
                        console.log(pitanja);
                        updateJsonDB($(this));
                        statistika();
                    })
                });
            </script>
            <?php
        }
        }
    }
}

function prikazi_pitanje($n, $i){

    $tip = $n['tipOdgovora'];
    $idp = $n['idPitanja'];
    $q = "SELECT * FROM potpitanja WHERE idPitanja='" . $idp . "' ORDER BY redpotp;";
    $qr1 = qq($q);

?>


<?php
$nas = 0;
$nas1 = 0;
$sel = 0;
$br_pp = $qr1->num_rows;
$red_p = $i - 1;
$ob="";
if($n['obavezno']=='1')$ob="*";
?>


    <div class="field hidden" id="pitanje[<?php echo $red_p; ?>]">
        <div class="naslov">
            <h3 class="ui header"> <?php echo $i . ". " . $n['tekst']."  ".$ob; ?></h3>
        </div>
        <?php
        $q2 = "SELECT * FROM izbor WHERE idPitanja='" . $idp . "';";
        $qr2 = qq($q2);
        $qr5 = qq($q2);
        $qr5 = $qr5->fetch_all(MYSQLI_ASSOC);
        $qr5 = json_encode($qr5);

        $br1 = 0;
        $br2 = 0;
        $br3 = 0;
        $br4 = 0;
        $br5 = 0;
        $br6 = 0;
        $br7 = 0;
        $k = 0;
        $k1 = 0;

        while ($n1 = $qr1->fetch_assoc()) {

            ?>
            <script>

                var tipOdg =<?php echo $tip; ?>;
                var p =<?php echo json_encode($n1); ?>;
                var izb =<?php echo $qr5; ?>;
                var br_pitanja =<?php echo $i; ?>;
                br_pitanja = br_pitanja - 1;
                var pp_i = [];
                var j = 0;
                var o = [];
                while (izbor = izb[j]) {
                    var oo = {
                        "idIzb": izbor['idIzbora'],
                        "teskt_izb": izbor['izbor'],
                        "val": null
                    };
                    o.push(oo);
                    j++;
                }
                //console.log(o);

                switch (tipOdg) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:

                        pp_i = {"idPP": p['idPotpitanja'],"tekst_pp":p['potpitanje'], "o": null};
                        break;


                    case 6:
                    case 7:
                        pp_i = {"idPP": p['idPotpitanja'],"tekst_pp":p['potpitanje'], "o": o};
                        break;
                }
                //console.log("tip: " + tipOdg + " - Pitanje:" + br_pitanja);
                //console.log(pp_i);
                pitanja[br_pitanja]['pp'].push(pp_i);
            // console.log(pitanja);
            </script>


        <?php
        if ($tip == 1){
        ?>

            <label><?php echo $n1['potpitanje']; ?></label>
        <input type="number" name="broj[<?php echo $br1; ?>]" class="izmenjiv question" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idt="1">


            <?php
            $br1++;
        } elseif ($tip == 2) {
            ?>

            <label><?php echo $n1['potpitanje']; ?></label>
        <input type="text" name="tekst[<?php echo $br2; ?>]" class="izmenjiv question" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idt="2">


            <?php
            $br2++;
        } elseif ($tip == 3) {
            ?>

            <label><?php echo $n1['potpitanje']; ?></label>
            <textarea name="unostekst[<?php echo $br3; ?>]" cols="40" rows="5" class="izmenjiv question" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idt="3"></textarea>


            <?php

            $br3++;
        } elseif ($tip == 4) {
            $sel += 1;
        if ($sel == 1) {
            ?>

            <select name="selizbor[<?php echo $br4; ?>]" class="izmenjiv question" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idt="4">
            <option value=""></option>
        <?php } ?>
            <option value="<?php echo $n1['potpitanje']; ?>"><?php echo $n1['potpitanje']; ?></option>
            <!--        </select>-->
            <?php
        if ($sel == $br_pp) {
            ?>
            </select>
            <?php
        }
            $br4++;
        } elseif ($tip == 5) {

            ?>
            <div>
                <input type="checkbox" class="check izmenjiv question" id="checkbox[<?php echo $br5; ?>]"
                       name="checkbox[<?php echo $br5; ?>]" value="" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idt="5">
                <label for="checkbox[<?php echo $br5; ?>]"><?php echo $n1['potpitanje']; ?></label>
            </div>
            <?php
            $br5++;
        }
        elseif ($tip == 6) {
            $nas += 1;

        if ($qr2->num_rows > 0){
            $qr22=qq($q2);
        if ($nas == 1){
            ?>

            <table class="ui celled table tabelaAnketa">
            <tr>
                <td></td>
                <?php  while ($n2 = $qr2->fetch_assoc()) {
//
                    ?>
                    <td><?php echo $n2['izbor'];
                        $k++; ?></td>

                <?php } ?>
            </tr>
        <?php } ?>
            <tr>
                <td><?php echo $n1['potpitanje']; ?></td>
                <?php $j1 = 0;
                while ($j1 < $k) {
                    $nn2=$qr22->fetch_assoc();
                    ?>
                    <td><input type="radio" id="" name="izbor[<?php echo $br6; ?>]"
                               class="izmenjiv question" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idizb="<?php echo $nn2['idIzbora']; ?>" idt="6"></td>

                    <?php $j1++;
                } ?> </tr> <?php
        if ($nas == $br_pp){ ?>  </table>
            <?php
        }
        }
            $br6++;
        }
        elseif ($tip == 7) {
            $nas1 += 1;
        if ($qr2->num_rows > 0) {
            $qr22=qq($q2);
        if ($nas1 == 1) {
            ?>

            <table class="ui celled table tabelaAnketa">
            <tr>
                <td></td>
                <?php while ($n2 = $qr2->fetch_assoc()) { ?>
                    <td><?php echo $n2['izbor'];
                        $k1++; ?></td>

                <?php } ?>
            </tr>
        <?php } ?>
            <tr>
                <td><?php echo $n1['potpitanje']; ?></td>
                <?php $j2 = 0;
                while ($j2 < $k1) {
                    $nn3=$qr22->fetch_assoc();
                    $nn2=$qr2->fetch_assoc();
                    ?>
                    <td><input type="checkbox" id="" name="chizbor[<?php echo $br7; ?>]"
                               value="<?php echo $nn2['izbor']; ?>" class="izmenjiv question" ob="<?php echo $n['obavezno']; ?>" ida="<?php echo $n1['idPotpitanja']; ?>" idb="<?php echo $i-1; ?>" idizb="<?php echo $nn3['idIzbora']; ?>" idt="7"></td>

                    <?php $j2++;
                } ?>
            </tr>
            <?php if ($nas1 == $br_pp) { ?>  </table>
                <?php
            }
        }
            $br7++;
        }

        } ?>
    </div>
    <script>

        function progress(proc) {
            $('#progressbar').progress({percent: proc});
        }


        function provera_anketa(){
            var i2=0;
            // console.log(1);
            // return false;
            por="";
            while(pitanja[i2]){

                if(pitanja[i2]['ob']==1){
                    j3=0;
                    tipOdg=parseInt(pitanja[i2]['tp']);
                    console.log("true",pitanja[i2]['pp'][j3]===true);
                    console.log("false",pitanja[i2]['pp'][j3]);
                    stoploop=true;
                    imatrue=false;
                    switch (tipOdg) {
                        case 1:
                        case 2:
                        case 3:


                            while(pitanja[i2]['pp'][j3]){
                                console.log(pitanja[i2]);
                                console.log(pitanja[i2]['pp'][j3]['o']);
                                if(pitanja[i2]['pp'][j3]['o']!=false && pitanja[i2]['pp'][j3]['o']!=null && pitanja[i2]['pp'][j3]['o']!=''){

                                }else{
                                    por+="Pitanje "+pitanja[i2]['tekst_p'] + " nije popunjeno <br>";
                                    break;
                                }
                                j3++;
                            }
                            break;
                        case 4:
                        case 5:
                            while(pitanja[i2]['pp'][j3]){
                                if(pitanja[i2]['pp'][j3]['o'])imatrue=true;
                                j3++;
                            }
                            if(!imatrue)por += "Pitanje " + pitanja[i2]['tekst_p'] + " nije popunjeno <br>";
                            break;

                        case 6:
                        case 7:
                            while(pitanja[i2]['pp'][j3] && stoploop) {
                                j4 = 0;

                                while (pitanja[i2]['pp'][j3]['o'][j4] && stoploop) {
                                    if (pitanja[i2]['pp'][j3]['o'][j4]['val'] != false && pitanja[i2]['pp'][j3]['o'][j4]['val'] != null && pitanja[i2]['pp'][j3]['o'][j4]['val'] != '') {
                                        imatrue=true;
                                    }
                                    j4++;
                                }
                                if(!imatrue)stoploop=false;

                                j3++;
                            }
                            if(!stoploop)por += "Pitanje " + pitanja[i2]['tekst_p'] + " nije popunjeno <br>";

                            break;
                    }

                }
                i2++;
            }
            console.log(por);
            if(por!=''){
                document.getElementById('porukadiv').innerHTML=por;
                document.getElementById('greskadiv').style.display='block';
            }else{
                var p = JSON.stringify(pitanja);
                // alert(document.getElementById('broj['+0+']').value);
                // console.log(p);
                $.get('inc/update_table.php', {pit: p, ida: anketa,proc: 100}, function (data) {
                    // console.log(data);
                    console.log(data);
                    window.location.href="pretragaAnketa.php";
                });

            }
        }

    </script>
    <?php
}

function prikazi_anketu_sluzb($idA,$listic)
{

    $q = "SELECT * FROM anketa WHERE idAnketa='" . $idA . "';";
    $qr = qq($q);
    if ($qr->num_rows == 1) {
        $niz = $qr->fetch_assoc();
        $naziv = $niz['naziv'];
        $dp = $niz['datum_pocetka'];
        $dk = $niz['datum_kraja'];
        $brstr = $niz['broj_strana'];
//        $qq1="SELECT * FROM ispitanik_anketa WHERE idAnketa='".$idA."' AND username='".$_SESSION['kime']."';";
//        $qr1=qq($qq1);
//
//        if($qr1->num_rows>0){
//            $nn=$qr1->fetch_assoc();
//            if($nn['popunjena']==1){$pop=1;}
//        }
//        $pop = $niz['popunjena'];
        if (date('Y-m-d') > $dk) {
            echo "<h3>Istekla anketa!</h3>";
            exit();
        } else {
            ?>
            <h1 class="ui header centar"><?php echo $naziv; ?></h1>

        <?php

        $q = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja 
                ON anketa_pitanja.idPitanja =pitanja.idPitanja
                WHERE anketa_pitanja.idAnketa='" . $idA . "' ORDER BY red;";

        $q1 = "SELECT * FROM anketa WHERE idAnketa='" . $idA . "';";
        $qr1 = qq($q1);
        $n1 = $qr1->fetch_assoc();
        $qr = qq($q);
        $r = qq($q);
        $r = $r->fetch_all(MYSQLI_ASSOC);
        $r = json_encode($r);

        if ($qr->num_rows > 0) {
        $i = 0; ?>

            <script>
                anketa =<?php echo $idA;?>;
                niz =<?php echo $r;?>;
                pitanja = [];
                var i = 0;
                while (p = niz[i]) {
                    var pit = {
                        "idP": p['idPitanja'],
                        "tp": p['tipOdgovora'],
                        "tekst_p": p['tekst'],
                        "pp": []
                    };
                    pitanja.push(pit);
                    i++;
                }
                // console.log(pitanja);

            </script>

            <form class="ui form " id="anketa" name="anketa" action="<?php echo $_SERVER['PHP_SELF']; ?>"
                  onsubmit="">

                <!--            return provera_anketa()-->
                <?php

                if ($n1['anonimna'] != 1 && $listic == 1) {
                    ?>
                    <div class="field">
                        <label>Ime</label>
                        <input type="text" name="ime" ob="1">
                    </div>
                    <div class="field">
                        <label>Prezime</label>
                        <input type="text" name="prezime" ob="1">
                    </div>

                    <?php
                }
                $pitstr = $qr->num_rows / $brstr;
                $br_pit = $qr->num_rows;
                $pon = true;
                while ($n = $qr->fetch_assoc()) {
                    $i += 1;
                    prikazi_pitanje($n, $i);
                } ?>

                <?php

                ?>
                <input class="ui right floated button" id="sledeca" value="Sledeca" type="button"
                       onclick="menjaj_str(true)">
                <input class="ui left floated button" id="prethodna" value="Prethodna" type="button"
                       onclick="menjaj_str(false)">
                <br><br><br>
                <input type="hidden" name="ida" value="<?php echo $idA; ?>">
<!--                <input class="ui button savedugme" value="ZAPAMTI" name="zapamtiAnketa" type="button">-->
                <button class="ui button submitdugme" name="potvrdiAnketa" type="button" onclick="submit_form()">
                    POTVRDI
                </button>
            </form>

            <div class="ui indicating progress" id="progressbar">
                <div class="bar">
                </div>
                <div class="label">Popunjenost ankete</div>
            </div>

            <script>
                function prikazi_pitanje(id, bool) {
                    var pitanje = document.getElementById('pitanje[' + id + ']');
                    if (pitanje) {
                        if (bool) {
                            pitanje.style.display = 'block';
                        } else {
                            pitanje.style.display = 'none';
                        }
                    }
                }

                trenutna_strana = 0;
                brstr =<?php echo $brstr;?>;
                br_pit =<?php echo $br_pit;?>;
                pitstr = Math.ceil(br_pit / brstr);

                console.log("pit str", pitstr);

                function menjaj_str(bool) {
                    for (i = 0; i < br_pit; i++) {
                        prikazi_pitanje(i, false);
                    }
                    if (bool === true) {
                        if (trenutna_strana + 1 > brstr) return;
                        var pocetak = trenutna_strana * pitstr;
                        console.log("poc true", pocetak);
                        for (i = pocetak; i < pocetak + pitstr; i++) {
                            prikazi_pitanje(i, true);
                        }
                        trenutna_strana = trenutna_strana + 1;
                    }
                    if (bool === false) {
                        if (trenutna_strana - 1 === 0) return;
                        var pocetak = (trenutna_strana - 2) * pitstr;
                        console.log("poc false", pocetak);
                        for (i = pocetak; i < pocetak + pitstr; i++) {
                            prikazi_pitanje(i, true);
                        }
                        trenutna_strana = trenutna_strana - 1;
                    }


                    if (trenutna_strana <= 1) {
                        document.getElementById('prethodna').style.display = 'none';
                    } else {
                        document.getElementById('prethodna').style.display = 'block';
                    }

                    if (trenutna_strana >= brstr) {
                        document.getElementById('sledeca').style.display = 'none';
                    } else {
                        document.getElementById('sledeca').style.display = 'block';
                    }
                }


                menjaj_str(true);

                function updateJsonDB(data) {
                    var id_potp = data[0].getAttribute("ida");
                    var id_pita = data[0].getAttribute("idb");
                    var id_tip = data[0].getAttribute("idt");
                    // console.log(id_pita);
                    // console.log(id_potp);
                    // console.log(id_tip);
                    // console.log(data[0].value);
                    // console.log(pitanja[id_pita]['tp']);

                    if (pitanja[id_pita]['tp'] === '5') {
                        var i = 0;
                        while (pitanja[id_pita]['pp'][i]) {
                            if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                pitanja[id_pita]['pp'][i]['o'] = data[0].checked;
                                // console.log(pitanja);
                                break;
                            }
                            i++;
                        }
                    } else {
                        if (pitanja[id_pita]['tp'] === '6') {


                            var id_izb = data[0].getAttribute("idizb");
                            console.log("idIzb", id_izb);
                            console.log("idPotp", id_potp);
                            var i = 0;
                            while (pitanja[id_pita]['pp'][i]) {
                                j1 = 0;
                                if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                    while (pitanja[id_pita]['pp'][i]['o'][j1]) {
                                        // console.log("id_izb iz json",pitanja[id_pita]['pp'][i]['o'][j1]['idIzb'],"id_izb",id_izb);
                                        if (pitanja[id_pita]['pp'][i]['o'][j1]['idIzb'] === id_izb) {
                                            pitanja[id_pita]['pp'][i]['o'][j1]['val'] = data[0].checked;
                                            // alert(pitanja[id_pita]['pp'][i]['o'][j1]['val'] );
                                        } else {
                                            pitanja[id_pita]['pp'][i]['o'][j1]['val'] = false;
                                        }
                                        // console.log(pitanja[id_pita]['pp'][i]['o']);
                                        j1++;
                                    }
                                    //console.log(pitanja);
                                    // console.log(data[0]);

                                }
                                i++;
                            }
                        } else {
                            if (pitanja[id_pita]['tp'] === '7') {
                                var id_izb = data[0].getAttribute("idizb");
                                // alert(id_izb);
                                var i = 0;
                                while (pitanja[id_pita]['pp'][i]) {
                                    j1 = 0;
                                    if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                        while (pitanja[id_pita]['pp'][i]['o'][j1]) {
                                            if (pitanja[id_pita]['pp'][i]['o'][j1]['idIzb'] === id_izb) {
                                                pitanja[id_pita]['pp'][i]['o'][j1]['val'] = data[0].checked;

                                            }
                                            // console.log(pitanja[id_pita]['pp'][i]['o']);
                                            // alert( pitanja[id_pita]['pp'][i]['o'][j1]['val']);
                                            j1++;
                                        }
                                        console.log(pitanja);
                                        // console.log(data[0]);
                                    }
                                    i++;
                                }
                            } else {
                                if (pitanja[id_pita]['tp'] === '4') {
                                    var i = 0;
                                    while (pitanja[id_pita]['pp'][i]) {
                                        if (pitanja[id_pita]['pp'][i]['tekst_pp'] === data[0].value) {

                                            pitanja[id_pita]['pp'][i]['o'] = true;
                                        } else {
                                            pitanja[id_pita]['pp'][i]['o'] = false;
                                        }
                                        i++;
                                    }
                                } else {
                                    var i = 0;
                                    while (pitanja[id_pita]['pp'][i]) {
                                        if (pitanja[id_pita]['pp'][i]['idPP'] === id_potp) {
                                            pitanja[id_pita]['pp'][i]['o'] = data[0].value;
                                            console.log(pitanja);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                    // console.log(pitanja);
                }

                function statistika() {
                    i = 0;
                    odg = 0;
                    brp = pitanja.length;
                    while (pitanja[i]) {
                        // console.log("i=",i);
                        aa = true;
                        j = 0;
                        tp = pitanja[i]['tp'];
                        // console.log(pitanja[i]);
                        if (tp === '1' || tp === '2' || tp === '3') {
                            while (pitanja[i]['pp'][j]) {
                                if (pitanja[i]['pp'][j]['o'] == null || pitanja[i]['pp'][j]['o'] === '') {
                                    aa = false;
                                    break;
                                }
                                j++;
                            }
                            if (aa) {
                                odg++;
                            }
                            // console.log("aa",aa,"odg",odg);

                        } else {
                            if (tp === '4' || tp === '5') {
                                while (pitanja[i]['pp'][j]) {
                                    if (pitanja[i]['pp'][j]['o'] != null && pitanja[i]['pp'][j]['o'] !== false && pitanja[i]['pp'][j]['o'] !== '') {
                                        odg++;
                                        break;
                                    }
                                    j++;
                                }
                            }
                            // console.log(345);
                            if (tp === '6' || tp === '7') {
                                jj = 0;

                                var odg_pp = 0;
                                // console.log("tppp",tp);
                                while (pitanja[i]['pp'][j]) {
                                    console.log("BBBB", pitanja[i]['pp'][j]);
                                    odg_i = 0;
                                    jj = 0;
                                    while (pitanja[i]['pp'][j]['o'][jj]) {
                                        // console.log(pitanja[i]['pp'][j]['o'][jj]['val']);
                                        // alert(pitanja[i]['pp'][j]['o'][jj]['val']);
                                        console.log(pitanja[i]['pp'][j]['o'][jj]['val'], jj);
                                        if (pitanja[i]['pp'][j]['o'][jj]['val'] != null && pitanja[i]['pp'][j]['o'][jj]['val'] != false) {
                                            odg_i++;
                                        }
                                        // console.log((pitanja[i]['pp'][j]['o'][jj]['val']));
                                        jj++;
                                    }
                                    // alert(odg_i);
                                    if (odg_i > 0) {
                                        odg_pp++;
                                    }
                                    // console.log("heeej");
                                    j++;
                                }

                                // alert(pitanja[i]['pp'].length);
                                // console.log("OOOOOOO",odg_i);
                                // console.log("PPPPPP",pitanja[i]['pp'].length);
                                if (odg_pp >= pitanja[i]['pp'].length) {
                                    odg++;
                                }

                            }

                        }
                        i++;
                    }
                    proc = odg / brp * 100;
                    // console.log("odg",odg);
                    // console.log("brp",brp);
                    console.log("proc", proc);
                    progress(proc);
                }

                $(document).ready(function () {
                    // $('.savedugme').click(function(){
                    //     console.log(document.getElementsByName('ime'));
                    //     if(document.getElementsByName('ime').length>0 && document.getElementsByName('prezime').length>0){
                    //         if(document.getElementsByName('ime')[0].value!='' && document.getElementsByName('prezime')[0].value!='') {
                    //             var ime = document.getElementsByName('ime')[0].value;
                    //             var prezime = document.getElementsByName('prezime')[0].value;
                    //             var p = JSON.stringify(pitanja);
                    //             console.log('inc/update_table_sluzb.php?pit=' + p + '&ida=' + anketa + '&ime=' + ime + '&prezime=' + prezime);
                    //             console.log(p);
                    //             $.get('inc/update_table_sluzb.php', { pit: p , ida: anketa,ime:ime,prezime:prezime }, function(data){
                    //                 // console.log(data);
                    //             });
                    //         }
                    //     }else {
                    //         var p = JSON.stringify(pitanja);
                    //         console.log(p);
                    //         console.log('inc/update_table_sluzb.php?pit='+p+'&ida='+anketa);
                    //         $.get('inc/update_table_sluzb.php', {pit: p, ida: anketa}, function (data) {
                    //             // console.log(data);
                    //         });
                    //     }
                    // });

                });

                function submit_form() {
                    // $("#anketa").submit(function(){
                    // console.log(document.getElementsByName('ime'));
                    if (document.getElementsByName('ime').length > 0 && document.getElementsByName('prezime').length > 0) {
                        if (document.getElementsByName('ime')[0].value != '' && document.getElementsByName('prezime')[0].value != '') {
                            var ime = document.getElementsByName('ime')[0].value;
                            var prezime = document.getElementsByName('prezime')[0].value;
                            var p = JSON.stringify(pitanja);
                            console.log(p);
                            // alert('inc/update_table_sluzb.php?pit=' + p + '&ida=' + anketa + '&ime=' + ime + '&prezime=' + prezime);
                            $.get('inc/update_table_sluzb.php', {
                                pit: p,
                                ida: anketa,
                                ime: ime,
                                prezime: prezime
                            }, function (data) {
                                window.location.href = "pretragaAnketa.php";
                            });
                        }
                    } else {
                        var p = JSON.stringify(pitanja);
                        // alert(document.getElementById('broj['+0+']').value);
                        // console.log(p);
                        // alert('inc/update_table_sluzb.php?pit='+p+'&ida='+anketa);
                        $.get('inc/update_table_sluzb.php', {pit: p, ida: anketa}, function (data) {
                            console.log(data);
                            window.location.href = "pretragaAnketa.php";
                        });
                    }
                }

                // alert("Submitted");
                // });

                // console.log(pitanja);
                $(document).ready(function () {
                    var question = $(".question");
                    var ql = $(".question").length;
                    //alert(ql);
                    var br_rb = 0;
                    var proc = 0;
                    $(".question").change(function () {
                        updateJsonDB($(this));
                        console.log(pitanja);
                        statistika();
                    })
                });
            </script>
            <?php
        }
        }
    }
}
?>
