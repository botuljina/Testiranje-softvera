<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once('header.php');
?>
<script>odg_stat=[];</script>
<?php
if(isset($_GET['ida'])&& isset($_GET['idp'])) {
    $ida = $_GET['ida'];
    $idp = $_GET['idp'];
    $q = "SELECT * FROM ispitanik_anketa WHERE idAnketa='" . $ida . "' UNION SELECT * FROM listic_anketa WHERE idAnketa='" . $ida . "' ;";
    $qr = qq($q);
    ?>

            <script>
               res_all=<?php echo json_encode($qr->fetch_all()); ?>;
                   console.log(res_all);
               odg_stat=[];
        ida=<?php echo $ida;?>;
        idp=<?php echo $idp;?>;
console.log(res_all);
        for(i=0;i<res_all.length;i++){
            res=JSON.parse(res_all[i][3]);
            for(j=0;j<res.length;j++){
                if(res[j]['idP']==idp){
                    pitanje=res[j];
                    potpitanje=pitanje['pp'];
                    for(k=0;k<potpitanje.length;k++){

                        if(pitanje['tp']==6 || pitanje['tp']==7){
                            for(n=0;n<potpitanje[k]['o'].length;n++){

                                var odg_fake = {
                                    "o": potpitanje[k]['o'][n]['teskt_izb'],
                                    "tekst_pp": potpitanje[k]['tekst_pp'],

                                };
                                proveraRacun(odg_fake);
                            }
                        }else
                        proveraRacun(potpitanje[k]);
                    }
                }
            }
        }
               console.log(odg_stat);

        function proveraRacun(potpitanje){
            if(potpitanje['o']==null)return;
            var m=0;
            duz=odg_stat.length;
            for(m=0;m<duz;m++){
                if(potpitanje['o']==odg_stat[m]['odg'] && potpitanje['tekst_pp']==odg_stat[m]['tekst_pp'])break;
            }
            var odg = {
                "odg": potpitanje['o'],
                "tekst_pp": potpitanje['tekst_pp'],
                "br_p": 1
            };

            if(m<duz)odg_stat[m]['br_p']++;else odg_stat.push(odg);

        }

        function ukupnoOdg(tekstPP){
            var m=0;
            duz=odg_stat.length;
            var s=0;
            for(m=0;m<duz;m++){
                if(tekstPP==odg_stat[m]['tekst_pp'])s+=odg_stat[m]['br_p'];
            }
            return s;
        }

        function napraviTabeluIzvestaj(){
            console.log(odg_stat);
            document.getElementById('izvestajTab').innerHTML='';
            var i=0;
            table='';
            table+="<table class='ui celled table'>\n";
            table+="<thead>\n";
            table+="<tr>\n";
            table+="<th>Potpitanje</th>\n";
            table+="<th>Odgovor</th>\n";
            table+="<th>Broj ponavljanja</th>\n";
            table+="<th>Procenat ponavljanja</th>\n";
            // table+="<th></th>\n";
            table+="</thead>\n";
            table+="<tbody>\n";

            while(niz=odg_stat[i]){
                var row = "";
                row = napraviRedodg(niz);
                console.log(row);
                table += row;
                i++;
            }
            table+="</tbody>\n";
            table+="</table><br><br>\n";
            document.getElementById('izvestajTab').innerHTML=table;
        }

        function napraviRedodg(niz){
            var row = "";
            row += "<tr>\n";
            row+="<td>"+niz['tekst_pp']+"</td>";
            row+="<td>"+niz['odg']+"</td>";
            row+="<td>"+niz['br_p']+"</td>";
            perc=1.0*niz['br_p']/ukupnoOdg(niz['tekst_pp'])*100+" %";
            row+="<td>"+perc+"</td>";

            row += "</tr>\n";
            return row;
        }


</script>

    <?php
} ?>

<div id="izvestajTab"></div>
<script>
    napraviTabeluIzvestaj();
</script>