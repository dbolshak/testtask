import java.text.SimpleDateFormat
import java.text.DateFormat

int topicCount      = 10
int runningCount    = 10
int maxRecordsInCsv = 100

String currentDir      = new File(".").getAbsolutePath()
String separator       = System.getProperty("file.separator")
String baseDir         = "${currentDir}${separator}base_dir"
String timeStampFormat = "yyyy-MM-DD-HH-mm-ss"

(1..topicCount).each { topicId ->
    Thread.start {
        String historyFolder = new File("${baseDir}${separator}topic-${topicId}${separator}history").getAbsolutePath()

        (1..runningCount).each { timeStampDelta ->
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL)
            df.setTimeZone(TimeZone.getTimeZone("UTC"))
        
            SimpleDateFormat df1 = new SimpleDateFormat(timeStampFormat)
            df1.applyPattern(timeStampFormat)

            File folderForCsvFile = new File("${historyFolder}${separator}${df1.format(df.parse(df.format(new Date(946684800000 + timeStampDelta * 1000L))))}")
            if( !folderForCsvFile.exists() ) {
              folderForCsvFile.mkdirs()
            }
            File csvFile = new File ("${folderForCsvFile.getAbsolutePath()}${separator}offsets.csv")
            StringBuilder sb = new StringBuilder()
            (1..maxRecordsInCsv).each { partitionNumber ->
                sb.append(partitionNumber).append(",").append(Math.abs(new Random().nextInt() % 600 + 1)).append(System.getProperty("line.separator"))
            }
            csvFile.append(sb)
        }
    }
}
