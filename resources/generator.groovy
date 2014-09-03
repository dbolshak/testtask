import java.text.SimpleDateFormat;
import java.text.DateFormat;

int topicCount = 10
int runningCount = 10
int maxRecordsInCsv = 100

(1..topicCount).each { topicId ->
    Thread.start {
        File file = new File("D:\\projects\\testtask\\resources\\base_dir\\topic-${topicId}\\history")
        file.mkdirs()
        String path =file.getAbsolutePath()

        (1..runningCount).each { timeStampDelta ->
            Date date = new Date((1220227200L + timeStampDelta) * 1000L)
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL)
            df.setTimeZone(TimeZone.getTimeZone("UTC"))
            String date1 = df.format(date)
            Date date2 = df.parse(date1)
        
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-DD-HH-mm-ss");
            df1.applyPattern("yyyy-MM-dd-hh-mm-ss");

            File folder = new File("${path}\\${df1.format(date2)}")
            if( !folder.exists() ) {
              folder.mkdirs()
            }
            File csvFile = new File ("${path}\\${df1.format(date2)}\\offsets.csv")
            StringBuilder sb = new StringBuilder()
            (1..maxRecordsInCsv).each { partitionNumber ->
                sb.append(partitionNumber).append(",").append(Math.abs(new Random().nextInt() % 600 + 1)).append(System.getProperty("line.separator"))
            }
            csvFile.append(sb)
        }
    }
}