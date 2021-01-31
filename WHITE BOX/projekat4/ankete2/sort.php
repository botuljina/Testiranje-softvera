<?php
require_once('inc/database.php');

if(isset($_GET["inputValue"]) && isset($_GET["inputValue1"])) {
    $por = $_GET["inputValue"];
    $kr = $_GET["inputValue1"];
    $q = "SELECT * FROM anketa";

//    if($por=='-1'){$por='0';}
    if($kr=='-1'){$kr='0';}
if($por=='0' && $kr=='1'){
    $q = "SELECT * FROM anketa ORDER BY naziv DESC";

}
elseif ($por=='0' && $kr=='2'){
    $q = "SELECT * FROM anketa ORDER BY datum_pocetka DESC";
}
elseif ($por=='0' && $kr=='3'){
    $q = "SELECT * FROM anketa ORDER BY datum_kraja DESC";
}
elseif ($por=='1' && $kr=='1'){
    $q = "SELECT * FROM anketa ORDER BY naziv ASC";
}
elseif ($por=='1' && $kr=='2'){
    $q = "SELECT * FROM anketa ORDER BY datum_pocetka ASC";
}
elseif ($por=='1' && $kr=='3'){
    $q = "SELECT * FROM anketa ORDER BY datum_kraja ASC";
}
$qr = qq($q);

    $qr=$qr->fetch_all(MYSQLI_ASSOC);
    echo json_encode($qr);
}