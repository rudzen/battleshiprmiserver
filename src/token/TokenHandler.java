package token;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import utility.Base64Coder;

public class TokenHandler {

    private static TokenHandler instance;
    
    // in minuttes.
    private static final int TOKEN_LIFETIME = 10;

    // 16 chars = 16 byte = 128 bit
    private final static String KEY = "Summer petrichor";
    private final static String SIGNATURE = "Tessaract, the four dimensional equivalent of a cube.";
    private final static String SIGNATURE_KEY = "Tetradecahedrons";

    public static TokenHandler getInstance() {
        if (instance == null) {
            instance = new TokenHandler();
        }
        return instance;
    }

    public String createToken(final String user_id) {
        if (user_id.contains("\n")) {
            throw new IllegalArgumentException("User Id cannot contain line breaks!");
        }
        final String signature = encrypt(SIGNATURE, SIGNATURE_KEY);
        final String timestamp = prettyTime();
        final String unixTime = String.valueOf(System.currentTimeMillis());
        final String token = signature + '\n' + user_id + "\n" + timestamp + '\n' + unixTime;
        return encrypt(token);
    }

    public String validateToken(final String token) {
        try {
            final String _data = decrypt(token);
            final String[] data = _data.split("\n");
            final String actual_signature = decrypt(data[0], SIGNATURE_KEY);
            if (!SIGNATURE.equals(actual_signature)) {
                System.err.println("Token signature doesn't match");
                return null;
            }

            final String _unixTime = data[3];
            final long unixTime = Long.parseLong(_unixTime);
            if (unixTime + toMillis(TOKEN_LIFETIME, TimeUnit.MINUTES) > System.currentTimeMillis()) {
                data[2] = prettyTime();
                data[3] = String.valueOf(System.currentTimeMillis());
                final StringBuilder sb = new StringBuilder();
                for (final String s : data) {
                    sb.append(s).append('\n');
                }
                return sb.toString();
            }
            System.err.println("Token timed out");
            return null;
        } catch (final Exception e) {
            return null;
        }
    }

    public static String getUserID(final String token) {
        try {
            final String _data = decrypt(token);
            final String[] data = _data.split("\n");
            return data[1];
        } catch (final Exception e) {
            return null;
        }
    }

    public static String getTimestamp(final String token) {
        try {
            final String _data = decrypt(token);
            final String[] data = _data.split("\n");
            return data[2];
        } catch (final Exception e) {
            return null;
        }
    }

    public static String getUnixTime(final String token) {
        try {
            final String _data = decrypt(token);
            final String[] data = _data.split("\n");
            return data[3];
        } catch (final Exception e) {
            return null;
        }
    }

    public static String encrypt(final String token) {
        return encrypt(token, KEY);
    }

    public static String encrypt(final String token, final String key) {
        try {
            final Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);
            final byte[] encrypted = cipher.doFinal(token.getBytes("UTF-8"));

            return Base64Coder.encodeLines(encrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(final String encrypted) {
        return decrypt(encrypted, KEY);
    }

    public static String decrypt(final String encrypted, final String key) {
        try {
            final Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());

            cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
            final String token = new String(cipher.doFinal(Base64Coder.decode(encrypted)));
            return token;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String prettyTime() {
        return prettyTime(System.currentTimeMillis());
    }

    private String prettyTime(final long millis) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        final int sec = cal.get(Calendar.SECOND);
        final String _sec = String.format("%02d", sec);
        final int min = cal.get(Calendar.MINUTE);
        final String _min = String.format("%02d", min);
        final int hour = cal.get(Calendar.HOUR_OF_DAY);
        final String _hour = String.format("%02d", hour);
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final String _day = String.format("%02d", day);
        final int month = cal.get(Calendar.MONTH) + 1;
        final String _month = String.format("%02d", month);
        final int year = cal.get(Calendar.YEAR);
        final String _year = Integer.toString(year);
        final String time = _hour + ":" + _min + ":" + _sec + " " + _day + "/" + _month + "-" + _year;
        return time;
    }

    @SuppressWarnings("fallthrough")
    private static int toMillis(int value, final TimeUnit unit) {
        switch (unit) {
            case DAYS:
                value *= 24;
            case HOURS:
                value *= 60;
            case MINUTES:
                value *= 60;
            case SECONDS:
                value *= 1000;
            case MILLISECONDS:
                return value;
            case MICROSECONDS:
                throw new UnsupportedOperationException("Microseconds is not supported");
            case NANOSECONDS:
                throw new UnsupportedOperationException("Nanoseconds is not supported");
            default:
                throw new UnsupportedOperationException("The timeunit " + unit + " is not supported");
        }
    }
}
