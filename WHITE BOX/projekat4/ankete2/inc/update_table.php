<?php
if (!isset($_SESSION))session_start();
require_once('database.php');

//print_r($_GET["pit"]);
if(isset($_GET["pit"]) && isset($_GET["ida"]) && isset($_GET["proc"])) {
  $pop=0;
//    echo 1;
    $pitanja = $_GET["pit"];
    $idA = $_GET["ida"];
    $proc = $_GET["proc"];
    if($proc==100){$pop=1;}
    $q1="SELECT * FROM ispitanik_anketa WHERE idAnketa='".$idA."' AND username='".$_SESSION['kime']."';";
    $qr1=qq($q1);
    print_r($qr1->fetch_all(MYSQLI_ASSOC));
    if($qr1->num_rows==0){
        echo "\n Nula redova\n";
        $q = "INSERT INTO ispitanik_anketa (idAnketa,username,rezultat,popunjena,tipPopunjavanja) VALUES ('".$idA."','".$_SESSION['kime']."','".$pitanja."','".$pop."',0);";
//        echo "\n";
//        echo $q;
//        echo "\n";
        $qr=qq($q);
    }elseif ($qr1->num_rows==1){
        $q = "UPDATE ispitanik_anketa SET rezultat='".$pitanja."',popunjena='".$pop."' WHERE idAnketa='".$idA."' AND username='".$_SESSION['kime']."';";
        print_r($q);
        $qr=qq($q);
        echo "BOKIIII";
    }
//    echo "Uspeh";
}