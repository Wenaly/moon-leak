import java.security.MessageDigest;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        System.out.println(getHWID());
    }

    public static String getHWID() {
        try {
            String hwid =
                    Base64.getEncoder().encodeToString(System.getenv("os").getBytes())
                            + Base64.getEncoder().encodeToString(System.getProperty("os.name").getBytes())
                            + Base64.getEncoder().encodeToString(System.getProperty("os.arch").getBytes())
                            + Base64.getEncoder().encodeToString(System.getProperty("user.name").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("COMPUTERNAME").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("SystemRoot").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("HOMEDRIVE").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("PROCESSOR_LEVEL").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("PROCESSOR_REVISION").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("PROCESSOR_IDENTIFIER").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("PROCESSOR_ARCHITECTURE").getBytes())
                            + Base64.getEncoder().encodeToString(System.getenv("NUMBER_OF_PROCESSORS").getBytes());

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hwid.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte b : byteData) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}