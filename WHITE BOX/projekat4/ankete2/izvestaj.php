<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');

if(isset($_GET['ida'])) {
    $ida = $_GET['ida'];
    $q = "SELECT * FROM ispitanik_anketa WHERE idAnketa='" . $ida . "';";
    $q1 = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja 
                ON anketa_pitanja.idPitanja =pitanja.idPitanja
                WHERE anketa_pitanja.idAnketa='" . $ida . "' ORDER BY red;";
    $qr = qq($q);
    $qr1 = qq($q1);
    $i=0;
    while ($n1 = $qr1->fetch_assoc()) {
        $i++;
        ?>
        <div class="ui  list">
            <a class="item pitanje_link" ida="<?php echo $ida;?>" idp="<?php echo $n1['idPitanja'];?>"><?php echo $i.". ".$n1['tekst']; ?></a>
        </div>
        <?php
    }
}
    ?>

<script>
    $('.pitanje_link').click(function(){
        var ida=$(this).attr('ida');
        var idp=$(this).attr('idp');
        // console.log(ida);
        // $.get('pretragaAnketa.php', { ida: ida}, function(data){
        //     console.log(data);
        // });
        window.location.href='prikazi_izvestaj.php?ida='+ida+'&idp='+idp;
    });
</script>
<!--    <script>-->
<!--       pit_stat=[];-->
<!---->
<!--   </script>-->
<!--    --><?php
////    while($n=$qr->fetch_assoc()){
////        $rez=$n['rez'];?>
<!--       <script>-->
<!--           rez=--><?php ////echo $rez;?><!--//;-->
<!--//            i=0;-->
<!--//            while(rez[i]){-->
<!--//                j=0;-->
<!--//                if(rez[i]['tp']!=='6' && rez[i]['tp']!=='7') {-->
<!--//                while(rez[i]['pp'][j]){-->
<!--//-->
<!--//                        if (rez[i]['pp'][j]['o'] != null && rez[i]['pp'][j]['o'] !== '' && rez[i]['pp'][j]['o'] !== false) {-->
<!--//                            if(rez[i]['tp']!=='5'){-->
<!--//                            pit_stat[i][j] = rez[i]['pp'][j]['o'];-->
<!--//                            }else{-->
<!--//                                pit_stat[i][j] = rez[i]['pp'][j]['tekst_potp'];-->
<!--//                            }-->
<!--//                        }-->
<!--//                        j++;-->
<!--//                    }-->
<!--//-->
<!--//                }-->
<!--//                else{-->
<!--//                    while(rez[i]['pp'][j]) {-->
<!--//                        m = 0;-->
<!--//                        while (rez[i]['pp'][j]['o'][m]) {-->
<!--//-->
<!--//                        if (rez[i]['pp'][j]['o'][m]['val'] != null && rez[i]['pp'][j]['o'][m]['val'] !== '' && rez[i]['pp'][j]['o'][m]['val'] !== false) {-->
<!--//                            pit_stat[i][j] = rez[i]['pp'][j]['o'];-->
<!--//                        }-->
<!--//                        m++;-->
<!--//                    }-->
<!--//                        j++;-->
<!--//                    }-->
<!--//                }-->
<!--//                i++;-->
<!--//            }-->
<!--//        </script>-->
<?php
////    }
////}
////    ?>