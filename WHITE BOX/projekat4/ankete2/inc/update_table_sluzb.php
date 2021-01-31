<?php
if (!isset($_SESSION))session_start();
require_once('database.php');

//print_r($_GET["pit"]);
if(isset($_GET["pit"]) && isset($_GET["ida"]) && isset($_GET["ime"]) && isset($_GET["prezime"])) {


    $pitanja = $_GET["pit"];
    $idA = $_GET["ida"];
    $ime = $_GET["ime"];
    $prezime = $_GET["prezime"];
//    $q1="SELECT * FROM listic_anketa WHERE idAnketa='".$idA."' AND username='".$ime." ".$prezime."';";
//    $qr1=qq($q1);
//    print_r($qr1->fetch_all(MYSQLI_ASSOC));

        echo "\n Nula redova\n";
        $q = "INSERT INTO listic_anketa (idAnketa,ime,prezime,rezultat,anonimna) VALUES ('".$idA."','".$ime."','".$prezime."','".$pitanja."',0);";
        echo "\n";
        echo $q;
        echo "\n";
        $qr=qq($q);

//    echo "Uspeh";
}elseif (isset($_GET["pit"]) && isset($_GET["ida"]) && !isset($_GET["ime"]) && !isset($_GET["prezime"])){
    $pitanja = $_GET["pit"];
    $idA = $_GET["ida"];
//    $q1="SELECT * FROM ispitanik_anketa WHERE idAnketa='".$idA."' AND username='anonimno';";
//    $qr1=qq($q1);
//    print_r($qr1->fetch_all(MYSQLI_ASSOC));
//    if($qr1->num_rows==0){
        echo "\n Nula redova\n";
        $q = "INSERT INTO listic_anketa (idAnketa,ime,prezime,rezultat,anonimna) VALUES ('".$idA."','anonimno','anonimno','".$pitanja."',1);";
        echo "\n";
        echo $q;
        echo "\n";
        $qr=qq($q);
//    }elseif ($qr1->num_rows==1){
//        $q = "UPDATE ispitanik_anketa SET rezultat='".$pitanja."' WHERE idAnketa='".$idA."' AND username='".$_SESSION['kime']."';";
//        $qr=qq($q);
//    }
}