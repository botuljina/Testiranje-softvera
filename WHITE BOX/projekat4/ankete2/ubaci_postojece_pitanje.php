<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');

if (isset($_GET["ida"]) && isset($_GET["idp"])) {
    $ank = $_GET["ida"];
    $pit = $_GET["idp"];
    $q = "SELECT * FROM anketa_pitanja WHERE idAnketa='".$ank."' AND idPitanja='".$pit."';";
    $qr=qq($q);
    if($qr->num_rows==0){
        $q="INSERT INTO anketa_pitanja (idAnketa,idPitanja) VALUES('".$ank."','".$pit."');";
        $qr=qq($q);
    }

}