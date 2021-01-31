<?php
if (!isset($_SESSION))session_start();
require_once('database.php');

//print_r($_GET["p"]);
if(isset($_GET["p"]) && isset($_GET["pp_uk"]) && isset($_GET["tip_uk"])&& $_GET["dat_kraja"] && isset($_GET["naz"]) && isset($_GET["anon"]) && isset($_GET["dat_pocetka"])) {?>

    <?php
    $p = $_GET["p"];
    $p=json_decode($p);
    $pp_uk = $_GET["pp_uk"];
    $pp_uk=json_decode($pp_uk);
    $izb_uk = $_GET["izb_uk"];
    $izb_uk=json_decode($izb_uk);
    $tip_uk = $_GET["tip_uk"];
    $tip_uk=json_decode($tip_uk);
    $ob_uk = $_GET["ob_uk"];
    $ob_uk=json_decode($ob_uk);
    $naz = $_GET["naz"];
    $dat_pocetka = $_GET["dat_pocetka"];
    $dat_kraja = $_GET["dat_kraja"];
    $anon = $_GET["anon"];
    $brstr = $_GET["brstr"];

            $q="INSERT INTO anketa (naziv,datum_pocetka,datum_kraja,autor,anonimna,broj_strana) VALUES ('" . $naz . "','" . $dat_pocetka . "','" . $dat_kraja . "','" . $_SESSION['kime'] . "','" . $anon . "','" . $brstr . "');";
        //print_r($q);
            $qr=qq($q);
    $qr=qq("SELECT LAST_INSERT_ID();");
    $qr=$qr->fetch_assoc();
    $ida=$qr['LAST_INSERT_ID()'];
    print_r($ida);
    $i=0;
    $j=0;

    foreach($p as $pit){
        //echo $pit;
            $k=0;
            echo "aaa";
            $q1="INSERT INTO pitanja (tekst,tipOdgovora) VALUES ('" . $pit. "','" . $tip_uk[$i] . "');";
            $qr1=qq($q1);
            $qr=qq("SELECT LAST_INSERT_ID();");
            $qr=$qr->fetch_assoc();
            $idpit=$qr['LAST_INSERT_ID()'];
            $ii=$i+1;
            $q1="INSERT INTO anketa_pitanja (idAnketa,idPitanja,obavezno,red) VALUES ('" . $ida. "','" . $idpit . "','" . $ob_uk[$i] . "','" . $ii . "');";
            $qr=qq($q1);
            $pp=$pp_uk[$i];
            foreach($pp as $potp) {
                    $k=$k+1;
                    $q2 = "INSERT INTO potpitanja (idPitanja,potpitanje,redpotp) VALUES ('" . $idpit . "','" . $potp . "','" . $k . "');";
                $qr2 = qq($q2);
                if ($tip_uk[$i] == 6 || $tip_uk[$i] == 7) {
                    $iz=$izb_uk[$j];

                        foreach ($iz as $izb) {

                            $q3 = "INSERT INTO izbor (idPitanja,izbor) VALUES ('" . $idpit . "','" . $izb . "');";
                            $qr3 = qq($q3);
                        }

                    $j++;
                }

            }


        $i++;
    }
    header("location: ../autor.php");
}