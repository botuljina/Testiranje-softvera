<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');



$q="SELECT * FROM anketa WHERE autor='".$_SESSION['kime']."';";
$qr=qq($q);?>
<table class="ui celled  table " id="tabelapregled">
            <thead>
            <tr>
                <th>Anketa</th>
                <th>Datum pocetka</th>
                <th>Datum kraja</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <?php
            if($qr->num_rows>0){
                while($n=$qr->fetch_assoc()){
                    ?>
                    <tr>
                        <td><?php echo $n['naziv']; ?></td>
                        <td><?php echo $n['datum_pocetka']; ?></td>
                        <td><?php echo $n['datum_kraja']; ?></td>
                        <td><button class="ui button pregleddugme"  value='<?php echo $n['idAnketa']; ?>' onclick="pregled(this.value)">PREGLED</button>
                        &nbsp;<button class="ui button izvestajdugme"  value='<?php echo $n['idAnketa']; ?>' onclick="izvestaj(this.value)">IZVESTAJ</button></td>
                    </tr>
                    <?php
                }
            }
            ?>
            </tbody>
        </table>

<script>
    function pregled(ida){
        window.location.href="pregled.php?ida="+ida;
    }
    function izvestaj(ida){
        window.location.href="izvestaj.php?ida="+ida;
    }
</script>
