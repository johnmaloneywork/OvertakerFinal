package GeneticAlgorithm;

import ahooraDriver.MySensorModel;

import java.util.Arrays;

public class GA {

    private Population population;
    private int currentGeneration;
    private int currentDriver;
    private final int maxGenerations;
    private final Selection selection = new Selection();
    private final Mutation mutation = new Mutation();
    private final Crossover crossover = new Crossover();

    public GA(int maxGenerations){
        this.maxGenerations = maxGenerations;
    }

    public IndividualTest[] evolve(IndividualTest[] individuals){

        //AFTER EACH GENERATION IS COMPLETE
        System.out.println("\nGENERATION " + (GeneticDriverController.generationCounter - 1) + " FINISHED" +
                "\nEVOLVING POPULATION");

        Population population = new Population(40);

        System.out.println("\nADVANCING TOP 10 DRIVERS");
        //Returns best 10 drivers
        IndividualTest[] best10 = selection.bestTenDrivers(individuals);
        //System.out.println(Arrays.toString(best10));

        //Returns the 30 winners of tournament selection
        IndividualTest[] tournament30 = selection.tournamentSelection(individuals);
        //System.out.println(Arrays.toString(tournament30));

        IndividualTest[] crossover15 = new IndividualTest[15];
        int crossoverCounter = 0;

        System.out.println("PERFORMING CROSSOVER");

        //15 individuals' genes are crossed from the 30 tournament winners
        for (int i = 0; i < tournament30.length; i+=2) {
            IndividualTest individualTest = crossover.singlePointCrossover(individuals[i], individuals[i + 1]);
            crossover15[crossoverCounter] = individualTest;
            crossoverCounter++;
        }

        //The best 10 and crossed 15 are added onto the next generation
        IndividualTest[] nextGeneration = new IndividualTest[individuals.length];   //population.getPOPULATION_SIZE()];

        for (int i = 0; i < best10.length; i++){
            nextGeneration[i] = best10[i];
            //System.out.println(nextGeneration[i].getId());
        }

        //System.out.println(Arrays.toString(crossover15));

        for (int j = 0; j < crossover15.length; j++){
            nextGeneration[j + best10.length] = crossover15[j];
            //System.out.println(nextGeneration[j + best10.length].getId());
        }

        //The remaining 15 individuals in the next population are generated
        for (int k = crossover15.length + best10.length; k < population.getPOPULATION_SIZE(); k++){
//            double[] speedGenes = new double[2];
//            double[] opponentSensorGenes = new double[8];
//            double[] trackSensorGenes = new double[6];
            double[] allGenes = new double[16];
            IndividualTest individualTest = new IndividualTest(allGenes);
            nextGeneration[k] = individualTest;
            //System.out.println(individualTest.getId());
        }

        population.setChromosomes(nextGeneration);

        //Mutation occurs at rate of 4%
        mutation.scrambleMutation(nextGeneration);

        System.out.println("\nPOPULATION EVOLVED");
        System.out.println("\nNEXT GENERATION");
        System.out.println(Arrays.toString(nextGeneration));

        return nextGeneration;
    }

    public IndividualTest[] runFirstGeneration(){
        
        Population firstGeneration  = new Population(40);
        currentGeneration++;

        System.out.println("THIS IS THE FIRST GENERATION METHOD");
        System.out.println("\nGENERATION " + GeneticDriverController.generationCounter + " IS RUNNING");
        System.out.println("POPULATION CREATED");

        System.out.println(Arrays.toString(firstGeneration.getChromosomes()));
        return firstGeneration.getChromosomes();
    }

    public IndividualTest[] runContinuedGeneration(){
        Population firstGeneration  = new Population(40);
        //Population continuedGeneration = new Population(10);
        IndividualTest driverOne = new IndividualTest();
        driverOne.setId("oZ3vo925rq");
        double[] driverOneGenes = {0.12283660663515561, 0.7402380165039671, 0.4455481623063493, 0.7321919064520461, 0.9847691030651208,
                0.06882770279158645, 0.5109885867321787, 0.8000630757233559, 0.40093957364726285, 0.09235845653844421, 0.6326287005839879,
                0.5204632174780947, 0.6857415783281857, 0.27860775561889806, 0.6084522273880832, 0.4521686224414979, 0.5894276890021988,
                0.7281740733432702, 0.8823064782184362, 0.042740275613443623, 0.995119405912192, 0.39317839287142};
        driverOne.setAllGenes(driverOneGenes);

        IndividualTest driverTwo = new IndividualTest();
        driverTwo.setId("E46HurtDes");
        double[] driverTwoGenes = {0.46980469017741555, 0.30719201819197284, 0.4250130737241382, 0.17893545945199285, 0.08364350211892713,
                0.20507898370222555, 0.9396310959927336, 0.9839648547243722, 0.9779766277479957, 0.15102744038122318, 0.9113721265037344,
                0.6949361072328188, 0.144594559779341, 0.6194173320592907, 0.5673433231218423, 0.7518195625025135, 0.5104848344002945,
                0.8750377807692552, 0.049761521441447076, 0.19596652986812235, 0.2512133232862822, 0.32155141118907227};
        driverTwo.setAllGenes(driverTwoGenes);

        IndividualTest driverThree = new IndividualTest();
        driverThree.setId("KPNHAdx6Cm");
        double[] driverThreeGenes ={0.9184890924259005, 0.7350686120388634, 0.4191979927663182, 0.47845201215371846, 0.70956977181793, 0.25814962058846,
                0.8863377053217474, 0.7403829670758778, 0.21790773320866752, 0.3488668358308139, 0.8370914329066728, 0.9488335601948927, 0.037030319902394915,
                0.409811958777053, 0.1748285635798834, 0.30878951573995883, 0.8357024464778078, 0.2831210955853053, 0.5515288426654085, 0.9702143888996528,
                0.9800046154373889, 0.7629317424472267};
        driverThree.setAllGenes(driverThreeGenes);

        IndividualTest driverFour = new IndividualTest();
        driverFour.setId("CNsGl6tOkW");
        double[] driverFourGenes = {0.45777390586045863, 0.2756443812264108, 0.48234499444416135, 0.8722553699018933, 0.014941918838988433, 0.8065416146140812,
                0.556510013002157, 0.6974292194712839, 0.7276244157016071, 0.1136051592296321, 0.9906794881633179, 0.5419087137511334, 0.4810631304226253,
                0.6447143965604863, 0.5949737463841092, 0.31389814662471915, 0.5651902838954276, 0.4628810469215563, 0.6069083994765212, 0.38923944035751,
                0.717499953475358, 0.68631013740591};
        driverFour.setAllGenes(driverFourGenes);

        IndividualTest driverFive = new IndividualTest();
        driverFive.setId("fiXls4R5s3");
        double[] driverFiveGenes = {0.1174811745806964, 0.8203586882529568, 0.29162430743762746, 0.7302222549149324, 0.07615245045231744, 0.8039483877404692,
                0.4400934030090906, 0.3010126929510565, 0.9716304806313233, 0.24499359790974118, 0.3263928394276824, 0.5843181645286107, 0.6283978219267418,
                0.1388410055873448, 0.8355250103608457, 0.4928813598486841, 0.6344613911031544, 0.8965766507306874, 0.2177944619724499, 0.5613130380018899,
                0.4951082620258527, 0.5411344378672388};
        driverFive.setAllGenes(driverFiveGenes);

        IndividualTest driverSix = new IndividualTest();
        driverSix.setId("asgBmXZAdn");
        double[] driverSixGenes = {0.20171550046044817, 0.45162430474878856, 0.6248253870115931, 0.7630695994276593, 0.47055858742288414, 0.8780661006128063,
                0.14625165070715884, 0.09566684621849486, 0.038249557659724154, 0.7459449467732794, 0.6074838236219926, 0.26321463970159, 0.05305945260008049,
                0.22096566640657478, 0.9101366451908903, 0.9820762690029982, 0.8345525168237627, 0.22891271869261243, 0.25361720222344286, 0.5676296297125486,
                0.23053808727980052, 0.6698639798378887};
        driverSix.setAllGenes(driverSixGenes);

        IndividualTest driverSeven = new IndividualTest();
        driverSeven.setId("NlLibnA68B");
        double[] driverSevenGenes = {0.05187109823160774, 0.37903966208423134, 0.5471961395755993, 0.7239373595571813, 0.5210450052866269, 0.3839582937464586,
                0.201699226580078, 0.22581256761575574, 0.4984288503844615, 0.3573576018566229, 0.6102278716685383, 0.8193853491707155, 0.9102834943308289,
                0.20216994548810696, 0.16086329990332682, 0.4194327445214284, 0.5887719416048205, 0.9937532139303834, 0.8085029726885903, 0.04324385693649746,
                0.9571683176858403, 0.7495508254963483};
        driverSeven.setAllGenes(driverSevenGenes);

        IndividualTest driverEight = new IndividualTest();
        driverEight.setId("l4fbyVtLyn");
        double[] driverEightGenes = {0.18956950822883578, 0.18430837997166094, 0.8920700738285487, 0.26420098608898845, 0.5178316910026237,
                0.5782715473686608, 0.47037966955374166, 0.27383874049825885, 0.5900687780427722, 0.8771881418905266, 0.33283664967702187,
                0.8569183204690107, 0.5698631615653904, 0.3703953218397579, 0.5901948656225988, 0.8809371825287768, 0.3172456985845781,
                0.22414627074612103, 0.723187996872418, 0.020155494358904957, 0.8224829605916927, 0.9621487822737163};
        driverEight.setAllGenes(driverEightGenes);

        IndividualTest driverNine = new IndividualTest();
        driverNine.setId("clwQIsXh4e");
        double[] driverNineGenes = {0.6451775434015667, 0.4835734107238877, 0.5926154722913379, 0.006246144349575378, 0.894464160825383, 0.8526990929998736,
                0.0506613772931257, 0.316823409100647, 0.9059890416796663, 0.18449384497493104, 0.7705541805135069, 0.0631736009327748, 0.8616661115935299,
                0.7017428518772371, 0.9103616978496164, 0.06570966373121945, 0.5810248374119114, 0.777250221520396, 0.37972603481355216, 0.963473601994164,
                0.19970576658487293, 0.561360695595104};
        driverNine.setAllGenes(driverNineGenes);

        IndividualTest driverTen = new IndividualTest();
        driverTen.setId("BvUQXxTrAs");
        double[] driverTenGenes = {0.1800627634585238, 0.2640380753742896, 0.6433284510673781, 0.0585659125328154, 0.1810380587399344, 0.8982908504902429,
                0.1555562654837791, 0.894281990004542, 0.009514738636549613, 0.6802883395738172, 0.9917673300918766, 0.3939146694491348, 0.5981795083148719,
                0.15992404386167702, 0.3993230243759416, 0.6441939066559612, 0.8782149523187854, 0.8454123845153306, 0.06452925772368068, 0.45443182772459056,
                0.6945988005374553, 0.9134929730223512};
        driverTen.setAllGenes(driverTenGenes);

        IndividualTest driverEleven = new IndividualTest();
        driverEleven.setId("sPLuf8V2SH");
        double[] driverElevenGenes = {0.6781325258634403, 0.12337510563260368, 0.31136106360364857, 0.21687501878623772, 0.15749449624899403,
                0.19760131367588196, 0.48809476735019197, 0.1278538232632851, 0.2649122802056256, 0.5582236883115995, 0.4748986058065744,
                0.6705757661788245, 0.24656763951759142, 0.5581671126102545, 0.028175232629938485, 0.5958059179947842, 0.8168398317260814,
                0.41185724686318104, 0.9035305133377407, 0.9898974248832686, 0.22559114888129916, 0.6987579758690201};
        driverEleven.setAllGenes(driverElevenGenes);

        IndividualTest driverTwelve = new IndividualTest();
        driverTwelve.setId("fU15DSnH1k");
        double[] driverTwelveGenes = {0.06523072895097937, 0.9819387725145852, 0.6940932191562624, 0.4087835042813204, 0.5657654125122994,
                0.15084979699335732, 0.24764166053573733, 0.9230419741101248, 0.029264903240801443, 0.3612134156378308, 0.9309256980738734,
                0.1631878572535128, 0.10982098107832117, 0.8200187071512778, 0.09357067255134899, 0.8501276825481068, 0.5313927081644656,
                0.6864260011646254, 0.9870415248870259, 0.45707010813111093, 0.6119503668376051, 0.6029312830739302};
        driverTwelve.setAllGenes(driverTwelveGenes);

        IndividualTest driverThirteen = new IndividualTest();
        driverThirteen.setId("CJOyFV7RBb");
        double[] driverThirteenGenes = {0.60521074304602, 0.9510684355426194, 0.6131672361840732, 0.6221284853478649, 0.10338683330452902,
                0.17329665084642631, 0.5744665014255007, 0.7115339116212633, 0.08783604915098919, 0.8228211948581012, 0.8452028636972605,
                0.34362961097642875, 0.6470216035632723, 0.6567369261078619, 0.07746255925509649, 0.20088390842814308, 0.415837796649961,
                0.5830774510373584, 0.9465656756217541, 0.8900247920118566, 0.7961188555051878, 0.004383424300829164};
        driverThirteen.setAllGenes(driverThirteenGenes);

        IndividualTest driverFourteen = new IndividualTest();
        driverFourteen.setId("42rMXfO7FB");
        double[] driverFourteenGenes = {0.45610875118859606, 0.9271974927623428, 0.7518684579986309, 0.08793072608139352, 0.7901575902677714,
                0.3879874186934943, 0.4743460123579112, 0.8265280478894116, 0.007075158175781526, 0.8663413226044454, 0.3885557134345351,
                0.16137225895259966, 0.46454060733215574, 0.886723651850608, 0.03448148347162805, 0.6770181191511415, 0.2094434690289524,
                0.8891101132679848, 0.6313992026581017, 0.6594897829551978, 0.5151213915017085, 0.9052065753990928};
        driverFourteen.setAllGenes(driverFourteenGenes);

        IndividualTest driverFifteen = new IndividualTest();
        driverFifteen.setId("LqAw2o6eS3");
        double[] driverFifteenGenes = {0.8693885899820472, 0.737135774358028, 0.1258699622796856, 0.22858056887810219, 0.06703804907109101,
                0.6699220199840914, 0.9957880503403723, 0.22817092109364634, 0.09264823446659254, 0.7235611156772427, 0.7313673339709293,
                0.003861751994590379, 0.08950681383792547, 0.3507079352607305, 0.6819204979955196, 0.7241255183826706, 0.7747498299206634,
                0.42517140654192453, 0.6719604226763781, 0.424950832411785, 0.9892827549792857, 0.9690542695509606};
        driverFifteen.setAllGenes(driverFifteenGenes);

        IndividualTest driverSixteen = new IndividualTest();
        driverSixteen.setId("GnsrtVg5n3");
        double[] driverSixteenGenes = {0.8460269398256702, 0.9838486260681336, 0.5580014414111281, 0.4060501457199569, 0.5845852165567247,
                0.16142513048739615, 0.5573020944860698, 0.9433458316215272, 0.817073619886707, 0.034854780397608254, 0.5674848284896514,
                0.01013146489962069, 0.32871748878675033, 0.18322481237107524, 0.07287768593030741, 0.9416463789537334, 0.23249903655156534,
                0.5619421672023597, 0.40079220923599035, 0.4972938794125221, 0.7083673362076326, 0.9349883197823083};
        driverSixteen.setAllGenes(driverSixteenGenes);

        IndividualTest driverSeventeen = new IndividualTest();
        driverSeventeen.setId("DZnImqV2Xm");
        double[] driverSeventeenGenes = {0.35597912246542907, 0.2708811342965045, 0.407272765817431, 0.23748068612977502, 0.2540350532707294,
                0.2754303297034283, 0.027576782080510842, 0.694048284460464, 0.6674829089690847, 0.806323040112351, 0.6473892481022883,
                0.40313391900517725, 0.09893669863966248, 0.3391520396385157, 0.2829091327214388, 0.8322621608965806, 0.8118417293325486,
                0.40035530173470457, 0.18514378931003506, 0.1176933023673562, 0.20152193832133858, 0.8460566000871215};
        driverSeventeen.setAllGenes(driverSeventeenGenes);

        IndividualTest driverEighteen = new IndividualTest();
        driverEighteen.setId("4vcqdOYaD3");
        double[] driverEighteenGenes = {0.5555721985364005, 0.7571073190932632, 0.0055684684386363426, 0.004391237008992244, 0.6548405488318177,
                0.5201139207427823, 0.3431041396573401, 0.2454120758297007, 0.5237497918680915, 0.16335388335984935, 0.4286491817590511,
                0.8652392620688119, 0.7877712263535182, 0.9036713672685924, 0.2626191779978396, 0.19704876032872687, 0.38475117196611974,
                0.7010137948814081, 0.9012320216896966, 0.26944288364859414, 0.7179748424427261, 0.11128700128879765};
        driverEighteen.setAllGenes(driverEighteenGenes);

        IndividualTest driverNineteen = new IndividualTest();
        driverNineteen.setId("pSmtXpcabU");
        double[] driverNineteenGenes = {0.4918057267881022, 0.4922739954124654, 0.6650055161814573, 0.9028893066960083, 0.41985516536030476,
                0.5471131451590484, 0.02958917659931304, 0.29226968554879773, 0.6749260090024574, 0.11990460677219483, 0.6521966427587919,
                0.15336722130004343, 0.22923705642332537, 0.3204734098207558, 0.08544086205103574, 0.646279056382742, 0.5900899799327382,
                0.900977993153284, 0.5811971777343155, 0.9554445854162197, 0.6112664694878829, 0.8166961724726104};
        driverNineteen.setAllGenes(driverNineteenGenes);

        IndividualTest driverTwenty = new IndividualTest();
        driverTwenty.setId("Yj5fwfopJd");
        double[] driverTwentyGenes = {0.0529498057075396, 0.09323130879012465, 0.07219465251484869, 0.3971438907619216, 0.47917006065093015,
                0.3001837781872586, 0.6256147612979549, 0.718958670473897, 0.26111831247646755, 0.775904249264426, 0.3688313990746428,
                0.9341673850369951, 0.14296452488457612, 0.9671088758880484, 0.945943898712461, 0.18312120735085913, 0.8988475143011588,
                0.31069826926917954, 0.7623032524628782, 0.9337291725136938, 0.3831531641962671, 0.6623868457655573};
        driverTwenty.setAllGenes(driverTwentyGenes);

        IndividualTest driverTwentyOne = new IndividualTest();
        driverTwentyOne.setId("fcfupf554q");
        double[] driverTwentyOneGenes = {0.8226088529321246, 0.49474565590324515, 0.9285882787027342, 0.02306716425610489, 0.4420274275808467,
                0.11769533927305242, 0.7399043146761594, 0.08222205529399829, 0.22042165791447488, 0.37858161621237096, 0.8853290247921201,
                0.8107345061464388, 0.8237751514074935, 0.5045048536467647, 0.41494248618383667, 0.16121131304485936, 0.6585629898264379,
                0.33356278726522925, 0.8098588752294267, 0.7121774261070922, 0.7949391967828165, 0.5467845108334867};
        driverTwentyOne.setAllGenes(driverTwentyOneGenes);

        IndividualTest driverTwentyTwo = new IndividualTest();
        driverTwentyTwo.setId("kIKqsj5lFN");
        double[] driverTwentyTwoGenes = {0.5059344527438805, 0.65428315369443, 0.9852850132556277, 0.12394904699242737, 0.5514261618838633,
                0.17016274909227225, 0.8777089328614822, 0.3963838722162476, 0.7582213567126044, 0.4824375753506679, 0.988431859701937,
                0.12879602860864836, 0.9515608704007841, 0.7344372097831736, 0.9069579788370241, 0.389277492140808, 0.8368288158889329,
                0.8366265372510463, 0.12075982799809182, 0.9859666627749203, 0.04414183013509587, 0.7349369205260358};
        driverTwentyTwo.setAllGenes(driverTwentyTwoGenes);

        IndividualTest driverTwentyThree = new IndividualTest();
        driverTwentyThree.setId("4VGNvy7yXy");
        double[] driverTwentyThreeGenes = {0.32108407104212067, 0.0752808077147038, 0.6567326965133854, 0.48947070788715297, 0.5430077518201011,
                0.2404661226489625, 0.8256394368393187, 0.11078335671283412, 0.19786145892587426, 0.019799578026062137, 0.965823748515781,
                0.2683225938690642, 0.9188612185444235, 0.44370479975525257, 0.518889629688588, 0.5172638648294069, 0.055622055431712836,
                0.07210237917046358, 0.28403864656965916, 0.4552730353600015, 0.9115448307805437, 0.5678718442743382};
        driverTwentyThree.setAllGenes(driverTwentyThreeGenes);

        IndividualTest driverTwentyFour = new IndividualTest();
        driverTwentyFour.setId("1Rd9vQsKDP");
        double[] driverTwentyFourGenes = {0.824125210963443, 0.13752221388399544, 0.9787464124679279, 0.44983711633322365, 0.9440529456864157, 0.17160514616337752,
                0.5421014322453449, 0.152392951432328, 0.0013600552514645425, 0.6487887840864246, 0.5703973355582704, 0.9593551924318412, 0.8678574758216305,
                0.231790814525331, 0.8887519584380756, 0.8219280342464047, 0.8717233856349252, 0.11250164461970447, 0.17507185113255097, 0.15255451445073043,
                0.3689842815289551, 0.44671839540512837};
        driverTwentyFour.setAllGenes(driverTwentyFourGenes);

        IndividualTest driverTwentyFive = new IndividualTest();
        driverTwentyFive.setId("JRqJPa732e");
        double[] driverTwentyFiveGenes = {0.020095359421113512, 0.5092748305396106, 0.6164764412789204, 0.18844776103357086, 0.9599351579828854, 0.409884136953635,
                0.2726024880810327, 0.5417379552368911, 0.369744475076623, 0.3595019987881197, 0.825940587355535, 0.30792483524119096, 0.6149755928702557,
                0.05324101402949466, 0.02768082786787207, 0.11384609819473812, 0.739377300641712, 0.02423952165546639, 0.629626364781849, 0.7417003633729036,
                0.5792194210326607, 0.23935730057949467};
        driverTwentyFive.setAllGenes(driverTwentyFiveGenes);

        IndividualTest driverTwentySix = new IndividualTest();
        driverTwentySix.setId("qsU2vA7OI1");
        double[] driverTwentySixGenes = {0.7565148238653705, 0.08070438313529349, 0.7371235576336759, 0.9112434489374083, 0.25593195998813256,
                0.9609606027989954, 0.7536988943288904, 0.676498416360934, 0.46210670756401306, 0.03100201203661157, 0.776764331470132,
                0.9265433420695232, 0.42678987993608297, 0.5756028868001721, 0.676237545250332, 0.34033628939937566, 0.8663663826471056,
                0.010219196922401919, 0.07900627287805773, 0.3965852671573211, 0.6647799097834632, 0.8628421361947816};
        driverTwentySix.setAllGenes(driverTwentySixGenes);

        IndividualTest driverTwentySeven = new IndividualTest();
        driverTwentySeven.setId("lRAroDW7Va");
        double[] driverTwentySevenGenes = {0.6832104429200577, 0.9136546244671097, 0.8106684111689276, 0.8489921185787339, 0.018133438782386047,
                0.7059943345746165, 0.4223853560138371, 0.2703650822200675, 0.16284186532777822, 0.36908467251154464, 0.9049325290619584,
                0.5625172292095154, 0.48031353493783946, 0.7555825976869344, 0.7533174687084898, 0.841011518640525, 0.8378140671846843,
                0.4655866091174098, 0.2967814800692937, 0.30894109578680795, 0.965460353844549, 0.88780453675816};
        driverTwentySeven.setAllGenes(driverTwentySevenGenes);

        IndividualTest driverTwentyEight = new IndividualTest();
        driverTwentyEight.setId("Wu3wxESIPL");
        double[] driverTwentyEightGenes = {0.5594472355017128, 0.9106310649623163, 0.015531763528549236, 0.49558126062705155, 0.9321516119611702,
                0.7688194200511624, 0.9639224297309195, 0.798743187197399, 0.9599382387698638, 0.7621124846637929, 0.4711135069039859,
                0.34495164833322, 0.22278039548098616, 0.3915573719318193, 0.38293752802880265, 0.3093259462972031, 0.1827782998708546,
                0.1417193119504646, 0.7207574477455019, 0.11463744695430278, 0.48827634843293954, 0.1406065195428482};
        driverTwentyEight.setAllGenes(driverTwentyEightGenes);

        IndividualTest driverTwentyNine = new IndividualTest();
        driverTwentyNine.setId("F4v4LcxdQQ");
        double[] driverTwentyNineGenes = {0.7360230905444688, 0.4674192458444024, 0.521784806106811, 0.8355291273259786, 0.6241179822425571,
                0.15580106696332718, 0.9597097350138557, 0.5262443872678539, 0.789161384672244, 0.6937692586611527, 0.9743954932896429,
                0.8310187542821498, 0.8828380602330982, 0.06302056418472957, 0.4040861794193882, 0.36060717334797965, 0.7489978692905348,
                0.6837019463719118, 0.9845322009650509, 0.49203790355355004, 0.1211958740419341, 0.046209292943561686};
        driverTwentyNine.setAllGenes(driverTwentyNineGenes);

        IndividualTest driverThirty = new IndividualTest();
        driverThirty.setId("hDq8KMURfI");
        double[] driverThirtyGenes = {0.081933792191067, 0.19109674632121254, 0.917984388239645, 0.04515047008398687, 0.11480792330559719,
                0.47317640497211044, 0.04182728993320384, 0.011746802656537714, 0.11385188059852724, 0.8208230438520742, 0.19044571457144832,
                0.5993338331123149, 0.5506820529474888, 0.9996299997345465, 0.2180688647636705, 0.8410463552644666, 0.24930055642067184,
                0.014021653436332437, 0.20407168174243673, 0.5081931234280352, 0.35120487872865447, 0.8118816107273558};
        driverThirty.setAllGenes(driverThirtyGenes);

        IndividualTest driverThirtyOne = new IndividualTest();
        driverThirtyOne.setId("wBb1XeW8Q2");
        double[] driverThirtyOneGenes = {0.21109243462680827, 0.43210906064918386, 0.3158122171253491, 0.5834553975437109, 0.6956519001276472,
                0.9436480367063133, 0.20711624846417764, 0.8906447749773526, 0.3692797775603599, 0.3293566434957109, 0.5250628111270579,
                0.8650403101865702, 0.3146772347011014, 0.26617834523926176, 0.4872700085411469, 0.08721682883929138, 0.0645817154127728,
                0.22210169911920097, 0.9539731730457288, 0.10391837445503949, 0.4103604582711632, 0.24914883881450067};
        driverThirtyOne.setAllGenes(driverThirtyOneGenes);

        IndividualTest driverThirtyTwo = new IndividualTest();
        driverThirtyTwo.setId("SrahX7OErt");
        double[] driverThirtyTwoGenes = {0.10486533063355952, 0.04032297193288914, 0.5436074647419954, 0.8231166478021439, 0.36982402680382087,
                0.003646313864410833, 0.44577857108341357, 0.8877524690610429, 0.28733443723501584, 0.7906483702283116, 0.4949306827158494,
                0.7733297418270888, 0.7681486318901374, 0.11663571524432448, 0.9791166159663497, 0.5678721582207285, 0.8436181357037066,
                0.9360961259010441, 0.9928872320478807, 0.48714868663771305, 0.09357590263345583, 0.1860085890449067};
        driverThirtyTwo.setAllGenes(driverThirtyTwoGenes);

        IndividualTest driverThirtyThree = new IndividualTest();
        driverThirtyThree.setId("SdrQIOQUt4");
        double[] driverThirtyThreeGenes = {0.9070782661095533, 0.4019991204202218, 0.9880849941676016, 0.7629369144341405, 0.6069722850459921, 0.4374435058719721,
                0.727853728958559, 0.49401040076116953, 0.8453704498600978, 0.27687226071786397, 0.07606829053783426, 0.17410206707992892, 0.17716648104923605,
                0.3530800327212755, 0.8346267265508855, 0.15000361114398686, 0.6898916648801589, 0.48228498660911967, 0.5803573443394389, 0.8307713518714227,
                0.07620733929764112, 0.49416820835585096};
        driverThirtyThree.setAllGenes(driverThirtyThreeGenes);

        IndividualTest driverThirtyFour = new IndividualTest();
        driverThirtyFour.setId("YknmG9sU8E");
        double[] driverThirtyFourGenes = {0.39314433697355133, 0.6794930896468635, 0.9295753405691505, 0.9651329248004391, 0.6722758689537546, 0.7193416025925088,
                0.48555126054474795, 0.33568896201772247, 0.8327942174746877, 0.10097904040504302, 0.2680125383440013, 0.9820826162172035, 0.1940442144075576,
                0.47814448391904174, 0.9385407777226978, 0.16887660943384242, 0.4921080323581746, 0.2561554191029052, 0.49606981539786443, 0.19454667671428028,
                0.8158285267149402, 0.2814093210909331};
        driverThirtyFour.setAllGenes(driverThirtyFourGenes);

        IndividualTest driverThirtyFive = new IndividualTest();
        driverThirtyFive.setId("NMyDKQQEmb");
        double[] driverThirtyFiveGenes = {0.5752352193038438, 0.4532783174713628, 0.5348851349555525, 0.34976369980247524, 0.3013003692284353, 0.977693925661528,
                0.7538085954624468, 0.6841810133765336, 0.18563519822179586, 0.5739199550812915, 0.8957874268472678, 0.058088944857453195, 0.6703632051187196,
                0.06381595737516832, 0.7669189876210577, 0.7397489644926971, 0.6085218979359748, 0.8139492712327138, 0.013042082861109305, 0.21095433247062134,
                0.7025264351608961, 0.4289027320744364};
        driverThirtyFive.setAllGenes(driverThirtyFiveGenes);

        IndividualTest driverThirtySix = new IndividualTest();
        driverThirtySix.setId("IigCDpWxZe");
        double[] driverThirtySixGenes = {0.009667427014853458, 0.7823197695447579, 0.9747214374959473, 0.06210708193966674, 0.6018831583879559, 0.023303829008733068,
                0.07638874925144501, 0.1392173864936086, 0.5403581862537498, 0.4683265487700402, 0.46573893632143337, 0.0391792855524602, 0.8815899808099612,
                0.39633720963558094, 0.8840913300543866, 0.45344268374513463, 0.22299371047975858, 0.4032492730921464, 0.7881649065236446, 0.22870921607891181,
                0.5076063460241119, 0.12618428964306583};
        driverThirtySix.setAllGenes(driverThirtySixGenes);

        IndividualTest driverThirtySeven = new IndividualTest();
        driverThirtySeven.setId("japo10THIE");
        double[] driverThirtySevenGenes = {0.20655859599001414, 0.18128024998473435, 0.9707035719790938, 0.5080107962037942, 0.9256596584983781, 0.07226650019504843,
                0.43959816230694004, 0.9606714681065762, 0.8305206892875739, 0.19178622165017223, 0.05078836565280376, 0.7892226593102142, 0.8255661225101907,
                0.8820236094929346, 0.5452023673942268, 0.8868230457490767, 0.6968894618489698, 0.2170540533820764, 0.9720589236474664, 0.6687596615564814,
                0.1147714036434383, 0.9013652425637474};
        driverThirtySeven.setAllGenes(driverThirtySevenGenes);

        IndividualTest driverThirtyEight = new IndividualTest();
        driverThirtyEight.setId("QfYclqcDAr");
        double[] driverThirtyEightGenes = {0.20949990392290108, 0.6795593189145666, 0.6657028012225257, 0.23028060216368007, 0.7530532384007936, 0.09117121718971766,
                0.14343945004663972, 0.7704331675643741, 0.19517268051736936, 0.14719637454844126, 0.6038539486890364, 0.17010335477601135, 0.6909751792318422,
                0.8367357139362631, 0.6049388655787858, 0.9872510037589459, 0.36114667117475374, 0.9352411328921553, 0.5466602820801034, 0.4028738351336564,
                0.9077493238003707, 0.06821786503688587};
        driverThirtyEight.setAllGenes(driverThirtyEightGenes);

        IndividualTest driverThirtyNine = new IndividualTest();
        driverThirtyNine.setId("PlELxPZws5");
        double[] driverThirtyNineGenes = {0.7913261592971043, 0.7024783521311624, 0.48751183416481614, 0.5458210494375, 0.020674894300438562, 0.12607009272056935,
                0.4982455726206104, 0.6161884265951009, 0.2868911499936352, 0.7199675489670481, 0.8126999513464732, 0.8163303057250052, 0.8290701861743185,
                0.5064373103013214, 0.8756175906325794, 0.6942616001979035, 0.6479553600714878, 0.11058063870449009, 0.7938733500931757, 0.659867367458455,
                0.9542580484380725, 0.9701607937572306};
        driverThirtyNine.setAllGenes(driverThirtyNineGenes);

        IndividualTest driverForty = new IndividualTest();
        driverForty.setId("c4lwiyipgg");
        double[] driverFortyGenes = {0.1306681917766772, 0.5897166909555197, 0.164954470247351, 0.7203492763664313, 0.3810571999452209, 0.9183210462704103,
                0.20975416143768721, 0.0194496896262778, 0.6985004557833836, 0.0016348419481861942, 0.43088659395469, 0.02642921482556193,
                0.29590355303014204, 0.6846417650538182, 0.22160562338991774, 0.33067690811592465, 0.10899654925433833, 0.4813055687480443,
                0.6955756752340898, 0.7262321380796036, 0.07384830676587217, 0.5878614626385823};
        driverForty.setAllGenes(driverFortyGenes);

        firstGeneration.getChromosomes()[0] = driverOne;
        firstGeneration.getChromosomes()[1] = driverTwo;
        firstGeneration.getChromosomes()[2] = driverThree;
        firstGeneration.getChromosomes()[3] = driverFour;
        firstGeneration.getChromosomes()[4] = driverFive;
        firstGeneration.getChromosomes()[5] = driverSix;
        firstGeneration.getChromosomes()[6] = driverSeven;
        firstGeneration.getChromosomes()[7] = driverEight;
        firstGeneration.getChromosomes()[8] = driverNine;
        firstGeneration.getChromosomes()[9] = driverTen;
        firstGeneration.getChromosomes()[10] = driverEleven;
        firstGeneration.getChromosomes()[11] = driverTwelve;
        firstGeneration.getChromosomes()[12] = driverThirteen;
        firstGeneration.getChromosomes()[13] = driverFourteen;
        firstGeneration.getChromosomes()[14] = driverFifteen;
        firstGeneration.getChromosomes()[15] = driverSixteen;
        firstGeneration.getChromosomes()[16] = driverSeventeen;
        firstGeneration.getChromosomes()[17] = driverEighteen;
        firstGeneration.getChromosomes()[18] = driverNineteen;
        firstGeneration.getChromosomes()[19] = driverTwenty;
        firstGeneration.getChromosomes()[20] = driverTwentyOne;
        firstGeneration.getChromosomes()[21] = driverTwentyTwo;
        firstGeneration.getChromosomes()[22] = driverTwentyThree;
        firstGeneration.getChromosomes()[23] = driverTwentyFour;
        firstGeneration.getChromosomes()[24] = driverTwentyFive;
        firstGeneration.getChromosomes()[25] = driverTwentySix;
        firstGeneration.getChromosomes()[26] = driverTwentySeven;
        firstGeneration.getChromosomes()[27] = driverTwentyEight;
        firstGeneration.getChromosomes()[28] = driverTwentyNine;
        firstGeneration.getChromosomes()[29] = driverThirty;
        firstGeneration.getChromosomes()[30] = driverThirtyOne;
        firstGeneration.getChromosomes()[31] = driverThirtyTwo;
        firstGeneration.getChromosomes()[32] = driverThirtyThree;
        firstGeneration.getChromosomes()[33] = driverThirtyFour;
        firstGeneration.getChromosomes()[34] = driverThirtyFive;
        firstGeneration.getChromosomes()[35] = driverThirtySix;
        firstGeneration.getChromosomes()[36] = driverThirtySeven;
        firstGeneration.getChromosomes()[37] = driverThirtyEight;
        firstGeneration.getChromosomes()[38] = driverThirtyNine;
        firstGeneration.getChromosomes()[39] = driverForty;

        currentGeneration++;

        System.out.println("THIS IS THE FIRST GENERATION METHOD");
        System.out.println("\nGENERATION " + GeneticDriverController.generationCounter + " IS RUNNING");
        System.out.println("POPULATION CREATED");

        System.out.println(Arrays.toString(firstGeneration.getChromosomes()));
        return firstGeneration.getChromosomes();
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(int currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public int getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(int currentDriver) {
        this.currentDriver = currentDriver;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public Selection getSelection() {
        return selection;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public Crossover getCrossover() {
        return crossover;
    }
}