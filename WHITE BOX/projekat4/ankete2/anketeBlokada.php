<?php
require_once 'inc/database.php';
require_once 'header.php';

if (!isset($_SESSION))
    session_start();

if (isset($_GET['block'])){
    $block=$_GET['block'];
    $username=$_GET['user'];
    $q="SELECT * FROM anketa WHERE autor='".$username."'";

    $qr=qq($q);
foreach($qr as $n){
    $q1="UPDATE anketa SET blokirana='".$block."'";
    qq($q1);
}

}


$q="SELECT * FROM autor;";
$qr=qq($q);
?>
<table class="ui celled  table " id="kortable">
    <thead>
    <tr>
        <th>Autor</th>
        <th>Status</th>
        <th>Deblokada</th>

    </tr>
    </thead>

    <tbody>
    <?php
    if($qr->num_rows>0){
        while($n=$qr->fetch_assoc()) {
?>
                <tr>
                    <script>user=<?php echo json_encode($n['username']); ?>;</script>
                    <td><?php echo $n['username']; ?></td>
                    <td><button class="ui button brisiuser"  id="" onclick="blokada(true,user);" type="button" >BLOKIRAJ</button></td>
                    <td><button class="ui button brisiuser"  id="" onclick="blokada(false,user);" type="button" >DEBLOKIRAJ</button></td>

                </tr>
                <?php

        }
    }

    ?>
<script>
    function blokada(blokiraj, user){
        block=0;
        if(blokiraj)block=1;
        window.location.href='anketeBlokada.php?block='+block+'&user='+user;

    }

</script>