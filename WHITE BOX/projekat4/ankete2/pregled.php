<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');

if(isset($_GET['ida'])){
    $ida=$_GET['ida'];
    $q="SELECT * FROM ispitanik_anketa INNER JOIN anketa ON ispitanik_anketa.idAnketa=anketa.idAnketa  WHERE ispitanik_anketa.idAnketa='".$ida."';";
    $q4="SELECT * FROM listic_anketa INNER JOIN anketa ON listic_anketa.idAnketa=anketa.idAnketa  WHERE listic_anketa.idAnketa='".$ida."';";
    $qr=qq($q);
    $qr4=qq($q4);
    ?>
    <table class="ui celled  table " id="tabelapregled">
        <thead>
        <tr>

            <th>Anketa</th>
            <th>Ime</th>
            <th>Prezime</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <?php
        if($qr->num_rows>0){
            while($n=$qr->fetch_assoc()){

                $q1="SELECT * FROM ispitanik WHERE username='".$n['username']."' UNION SELECT * FROM sluzbenik WHERE username='".$n['username']."' UNION SELECT * FROM autor WHERE username='".$n['username']."';";
                $qr1=qq($q1);
                $n1=$qr1->fetch_assoc();
                ?>
                <tr>
                    <td><?php echo $n['naziv']; ?></td>
                    <?php
                    $anon=0;
                    if($n['anonimna']==1){
                        $anon=1;
                        ?>

                        <td>Anonimno</td>
                        <td>Anonimno</td>
                    <?php

                    }else{?>
                    <td><?php echo $n1['ime']; ?></td>
                    <td><?php echo $n1['prezime']; ?></td>
                    <?php }?>

                    <td><button class="ui button linkdugme"  ida='<?php echo $n['idAnketa']; ?>' idpp='<?php echo $n['idPopunjavanje']; ?>' tip='2' anon="<?php echo $anon; ?>">POGLEDAJ</button>
                    </td>
                </tr>
                <?php
            }
        }
        if($qr4->num_rows>0){
            while($n=$qr4->fetch_assoc()){

                ?>
                <tr>
                    <td><?php echo $n['naziv']; ?></td>
                    <?php
                    $anon=0;
                    if($n['anonimna']==1){
                        $anon=1;
                        ?>

                        <td>Anonimno</td>
                        <td>Anonimno</td>
                        <?php

                    }else{?>
                        <td><?php echo $n['ime']; ?></td>
                        <td><?php echo $n['prezime']; ?></td>
                    <?php }?>

                    <td><button class="ui button linkdugme"  ida='<?php echo $n['idAnketa']; ?>' idpp='<?php echo $n['idListic']; ?>' tip='1' anon="<?php echo $anon; ?>">POGLEDAJ</button>
                    </td>
                </tr>
                <?php
            }
        }
        ?>
        </tbody>
    </table>
<script>
    $('.linkdugme').click(function(){
        var ida=$(this).attr('ida');
        var rez=$(this).attr('rez');
        var anon=$(this).attr('anon');
        var tip=$(this).attr('tip');
        var idpp=$(this).attr('idpp');
        // console.log("REZ",rez);
        document.getElementById('tabelapregled').style.display='none';

        window.location.href='pretragaAnketa.php?ida='+ida+'&id='+idpp+'&tip='+tip+'&anon='+anon;
    });
</script>
    <?php
}
