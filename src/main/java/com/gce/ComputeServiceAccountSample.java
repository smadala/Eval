package com.gce;

/**
 * Created by satya on 19/11/14.
 */

import com.gce.bean.TestBean;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.*;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComputeServiceAccountSample {

    private static final String APPLICATION_NAME = "kart";

    /** Set projectId to your Project ID from Overview pane in the APIs console */
    private static final String projectId = "radiant-shard-753";
    /** E-mail address of the service account. */
    private static final String SERVICE_ACCOUNT_EMAIL = "956537979008-vicsc6m5shh99cgvnir7a288sda52d1t@developer.gserviceaccount.com";

    /** Bucket to list. */
    private static final String BUCKET_NAME = "[[INSERT_YOUR_BUCKET_NAME_HERE]]";
    private static final String zoneName = "asia-east1-a";

    private static final String KEY_PATH="/home/satya/key.p12";

    /** Global configuration of Google Cloud Storage OAuth 2.0 scope. */
    private static final String COMPUTE_SCOPE =
            "https://www.googleapis.com/auth/compute";

    /** Global instance of the HTTP transport. */
   // private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static Compute getCompute(){
        try {
            try {
              //  InitConfigure.setProxy();
                HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                // Check for valid setup.
                Preconditions.checkArgument(!SERVICE_ACCOUNT_EMAIL.startsWith("[["),
                        "Please enter your service account e-mail from the Google APIs "
                                + "Console to the SERVICE_ACCOUNT_EMAIL constant in %s",
                        ComputeServiceAccountSample.class.getName());
               /* Preconditions.checkArgument(!BUCKET_NAME.startsWith("[["),
                        "Please enter your desired Google Cloud Storage bucket name "
                                + "to the BUCKET_NAME constant in %s", ComputeServiceAccountSample.class.getName());*/
                String p12Content = Files.readFirstLine(new File(KEY_PATH), Charset.defaultCharset());
                Preconditions.checkArgument(!p12Content.startsWith("Please"), p12Content);

                //[START snippet]
                // Build a service account credential.
                GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                        .setJsonFactory(JSON_FACTORY)
                        .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                        .setServiceAccountScopes(Collections.singleton(COMPUTE_SCOPE))
                        .setServiceAccountPrivateKeyFromP12File(new File(KEY_PATH))
                        .build();


                Compute compute = new Compute.Builder(
                        httpTransport, JSON_FACTORY, null).setApplicationName(APPLICATION_NAME)
                        .setHttpRequestInitializer(credential).build();

                // List out instances
                //printInstances(compute, projectId);


                // Success!
                return compute;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
               /* // Set up and execute a Google Cloud Storage request.
                String URI = "https://storage.googleapis.com/" + BUCKET_NAME;
                HttpRequestFactory requestFactory = httpTransport.createRequestFactory(credential);
                GenericUrl url = new GenericUrl(URI);
                HttpRequest request = requestFactory.buildGetRequest(url);
                HttpResponse response = request.execute();
                String content = response.parseAsString();
                //[END snippet]

                // Instantiate transformer input.
                Source xmlInput = new StreamSource(new StringReader(content));
                StreamResult xmlOutput = new StreamResult(new StringWriter());

                // Configure transformer.
                Transformer transformer = TransformerFactory.newInstance().newTransformer(); // An identity
                // transformer
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "testing.dtd");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, xmlOutput);

                // Pretty print the output XML.
                System.out.println("\nBucket listing for " + BUCKET_NAME + ":\n");
                System.out.println(xmlOutput.getWriter().toString());
                System.exit(0);*/

        return null;
    }
    /**
     * Print available machine instances.
     *
     * @param compute The main API access point
     * @param projectId The project ID.
     */
    public static void printInstances(Compute compute, String projectId) throws IOException {
        System.out.println("================== Listing Compute Engine Instances ==================");

        Compute.Instances.List instances = compute.instances().list(projectId, zoneName);

        InstanceList list = instances.execute();
        if (list.getItems() == null) {
            System.out.println("No instances found. Sign in to the Google APIs Console and create "
                    + "an instance at: code.google.com/apis/console");
        } else {
            for (Instance instance : list.getItems()) {
                System.out.println(instance.toPrettyString());
            }
        }

    }

    //private static final String DEFAULT_SERVICE_
    public static List<ServiceAccount> getDefaultServiceAccountList(){
        ServiceAccount serviceAccount=new ServiceAccount();
        serviceAccount.setEmail("default");//"956537979008-compute@developer.gserviceaccount.com"
        String [] scopes={"https://www.googleapis.com/auth/userinfo.email","https://www.googleapis.com/auth/compute",
                "https://www.googleapis.com/auth/devstorage.full_control",
                "https://www.googleapis.com/auth/taskqueue",
                "https://www.googleapis.com/auth/bigquery",
                "https://www.googleapis.com/auth/sqlservice",
                "https://www.googleapis.com/auth/datastore"};
        serviceAccount.setScopes(Arrays.asList(scopes));
        return Arrays.asList(serviceAccount);
    }
    private static final String ZONE_HTTP="https://www.googleapis.com/compute/v1/projects/radiant-shard-753/zones/asia-east1-a";
    private static final String SOURCE_SNAP_SHOT_HTTP="https://www.googleapis.com/compute/v1/projects/radiant-shard-753/global/snapshots/debian-main-1";
    private static final String DISK_KIND_HTTP="https://www.googleapis.com/compute/v1/projects/radiant-shard-753/zones/asia-east1-a/diskTypes/pd-standard";
    private static final long DEFAULT_DISK_SIZE=10l;
    private static void createDisk(Compute compute,TestBean testBean) {
        //testBean.testName="snapshot-3";
        Disk disk=new Disk();
        disk.setName(testBean.testName);
        disk.setSourceSnapshot(SOURCE_SNAP_SHOT_HTTP);
        disk.setZone(ZONE_HTTP);
        disk.setKind(DISK_KIND_HTTP);
        disk.setSizeGb(DEFAULT_DISK_SIZE);


        Operation operation = null;
        try {
            Compute.Disks.Insert insert =compute.disks().insert(projectId, zoneName, disk);
            operation = insert.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finishOperation(compute, operation);
       /* boolean created=false;
        int iterations=10;
        do{
            Thread.sleep(1000); //wait a second, let the disk created..
            DiskList diskList=list.execute();
            for(Disk disk1: diskList.getItems() ) {
                if( disk1.getName().equals(testBean.testName)) created=true;

            }
            iterations--;
            if(!created) System.out.println("Yet to create disk " + testBean.testName);
            else System.out.print(testBean.testName + ": disk created");
        }while (!created && iterations != 0);*/
    }
    private static final String ATTACHED_DISK_SOURCE_PREFIX="https://www.googleapis.com/compute/v1/projects/radiant-shard-753/zones/asia-east1-a/disks/";
    private static List<AttachedDisk> getAttachedDisk(Compute compute, TestBean testBean) {

        AttachedDisk attachedDisk=new AttachedDisk();

        createDisk(compute, testBean);
        attachedDisk.setBoot(true);
        attachedDisk.setType("PERSISTENT");
        attachedDisk.setDeviceName(testBean.testName);
        attachedDisk.setMode("READ_WRITE");
        attachedDisk.setSource(ATTACHED_DISK_SOURCE_PREFIX + testBean.testName);
        AttachedDisk [] attachedDisks={attachedDisk};
        return Arrays.asList(attachedDisks);
    }

    /*
    * "networkInterfaces": [
    {
      "network": "https://www.googleapis.com/compute/v1/projects/radiant-shard-753/global/networks/default",
      "accessConfigs": [
        {
          "name": "External NAT",
          "type": "ONE_TO_ONE_NAT"
        }
      ]
    }
  ],*/
    public static final String DEFAULT_NETWORK="https://www.googleapis.com/compute/v1/projects/radiant-shard-753/global/networks/default";
    private static List<NetworkInterface> getNetworkInterfaces(){
        NetworkInterface networkInterface=new NetworkInterface();
        networkInterface.setNetwork(DEFAULT_NETWORK);
        networkInterface.setAccessConfigs(new ArrayList<AccessConfig>());
        networkInterface.getAccessConfigs().add(new AccessConfig().setType("ONE_TO_ONE_NAT").setName("External NAT"));
        NetworkInterface []networkInterfaces={networkInterface};
        return Arrays.asList(networkInterfaces);
    }

    public static void deleteTestSetup(Compute compute, TestBean testBean)  {
        deleteInstance(compute, testBean.testName);
        deleteDisk(compute, testBean.testName);
    }

    private static final String MACHINE_TYPE_HTTP="https://www.googleapis.com/compute/v1/projects/radiant-shard-753/zones/asia-east1-a/machineTypes/n1-highcpu-2";
    public static void createInstance(Compute compute, TestBean testBean) {


        Instance instance=new Instance();



        instance.setName(testBean.testName);
        instance.setCanIpForward(false);

        instance.setServiceAccounts(getDefaultServiceAccountList());

        instance.setDisks(getAttachedDisk(compute, testBean));

        instance.setNetworkInterfaces(getNetworkInterfaces());

        instance.setZone(ZONE_HTTP);
        instance.setScheduling(new Scheduling().setAutomaticRestart(true).setOnHostMaintenance("MIGRATE"));

        instance.setMachineType(MACHINE_TYPE_HTTP);
        instance.setMetadata(new Metadata());
        instance.setTags(new Tags());



        Operation operation = null;
        try {
            Compute.Instances.Insert insert= compute.instances().insert(projectId,zoneName,instance);
            operation = insert.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finishOperation(compute, operation);
       /* System.out.println(operation.toPrettyString()+"\n\n");
        System.out.println(instance.toPrettyString());

        Compute.Instances.List listQuery =compute.instances().list(projectId, zoneName);
        boolean created=false;
        int iterations=10;
        do{
            Thread.sleep(5000);
            InstanceList instanceList= listQuery.execute();
            for(Instance instance1: instanceList.getItems()){
                if( instance1.getName().equals(testBean.testName)) {
                    created = true;
                    break;
                }else{
                    System.out.println(instance1.getName());
                }
            }
            iterations--;
            if(created) System.out.println(" Instance created :" + testBean.testName);
            else System.out.println(" Yet to create Instance :" + testBean.testName);
        }while (!created && iterations!= 0);

        printStatusOfOperation(compute, operation);*/
    }
    public static String printStatusOfOperation(Compute compute,Operation operation) throws IOException {
        Compute.ZoneOperations.Get get =compute.zoneOperations().get(projectId, zoneName, operation.getName());
        Operation operation1=get.execute();
        System.out.println(operation1.toPrettyString());
        return operation1.toPrettyString();
    }

    private static void deleteInstance( Compute compute, String instanceName)  {



        Operation operation= null;
        try {
            Compute.Instances.Delete deleteQuery = compute.instances().delete(projectId, zoneName, instanceName);
            operation = deleteQuery.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        finishOperation(compute, operation);

    }
    private static final Logger logger = LoggerFactory.getLogger(ComputeServiceAccountSample.class);
    public static void finishOperation(Compute compute, Operation operation) {
        int maxWaitIteration=60;
        try {
            do {
                if (isOperationCompleted(compute, operation)) {
                    break;
                } else
                    Thread.sleep(5000);
                maxWaitIteration--;
            } while (maxWaitIteration != 0);
            printStatusOfOperation(compute, operation);
            logger.info( " Details about Instance "+printStatusOfOperation(compute,operation));

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    enum ResourceType { INSTANCE, DISK };

    public static boolean isExist( String name, ResourceType resourceType){


     return false;
    }

    public static boolean isOperationCompleted(Compute compute, Operation operation) throws IOException {
        Compute.ZoneOperations.Get get =compute.zoneOperations().get(projectId, zoneName, operation.getName());
        Operation operation1=get.execute();
        System.out.println(operation1.toPrettyString());
        return operation1.getProgress() == 100;
    }


    public static void deleteDisk( Compute compute, String diskName) {

        Operation operation= null;
        try {
            Compute.Disks.Delete deleteQuery=compute.disks().delete(projectId, zoneName, diskName);
            operation = deleteQuery.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finishOperation(compute, operation);
    }
    //gcloud compute ssh "createdby-api" --command " java -jar JarApp.jar sample.prop;"  --zone=asia-east1-a
    //private static final String runCommand= " gcloud compute ssh \"{0}\" "
    //gcloud compute copy-files  createdby-api:/home/satya/result.prop  ~/copied.prop --zone=asia-east1-a
    private static final String HOME_FOLDER="/home/satya/";
    private static final String ZONE_IN_CMD="--zone=asia-east1-a";
    private static final String COPY_FILES_CMD="gcloud compute copy-files  {0}  {1} {2}";// source, destination, zone

    private static final String RUN_JOB_CMD="gcloud compute ssh \"{0}\" --command \" java -jar {1} {2};\"  {3}";
    //machinename, jarPath,
    public static void runTask(String machineName, String fromDir, String jarName, String destDir){

        String remoteDest=machineName+":"+destDir;
        String destJarPath=remoteDest+jarName, destInputPath=remoteDest+machineName;
        String copyJarCmd= MessageFormat.format(COPY_FILES_CMD, fromDir+jarName, destJarPath, ZONE_IN_CMD);
        String copyInputFileCmd=MessageFormat.format(COPY_FILES_CMD, fromDir+machineName, destInputPath, ZONE_IN_CMD);
        String runJobCmd=MessageFormat.format( RUN_JOB_CMD, machineName, destDir+jarName, destDir+machineName, ZONE_IN_CMD  );

        String copyResultFileCmd=MessageFormat.format(COPY_FILES_CMD, destInputPath+".result", fromDir, ZONE_IN_CMD);

        String[] env = {"PATH=/bin:/usr/bin/"};
        String allCommands = copyJarCmd+";"+copyInputFileCmd +";" +runJobCmd+";"+copyResultFileCmd;//e.g test.sh -dparam1 -oout.txt
        System.out.println(allCommands);
        try {
            Process process = Runtime.getRuntime().exec(allCommands, env);
            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}