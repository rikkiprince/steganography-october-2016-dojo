import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.imageio.ImageIO;

public class Steganography {

	public static byte[] encode(byte[] original, String plaintext) {
		// convert string into character array
		byte[] word_bytes = plaintext.getBytes();
	
		int image_byte = 0;
		// loop through image to store bits in LSB of image
		for(int i=0; i<word_bytes.length; i++) {
			byte current = word_bytes[i];
			for(byte bit=0; bit<8; bit++) {
				byte masked = (byte) ((current & (0x1<<bit))>>bit);
				System.out.print(masked);
				original[image_byte] = (byte) ((original[image_byte] & 0XFE) + masked);  
				image_byte++;
			}
			System.out.println();
		}
		return original;
	}
	
	public static String decode(byte[] cipherimage) throws UnsupportedEncodingException {
		int image_byte = 0;
		StringBuilder builder = new StringBuilder();
		byte[] out = new byte[13];
		int character=0;
		while(image_byte < 13*8/*cipherimage.length*/) {
			byte new_character = 0;
			for(byte bit=0; bit<8; bit++) {
				byte lsb = (byte) (cipherimage[image_byte]&0x1);
				new_character += lsb<<bit;
				image_byte++;
			}
			out[character] = new_character;
			character++;
			builder.append(new_character);
		}
		
		return new String(out, "UTF-8");
	}
	
	public static byte[] bufferedImageToBytes(BufferedImage bi) {
		List<Byte> list = new ArrayList<Byte>();
		for(int y=0; y<bi.getHeight(); y++) {
			for(int x=0; x<bi.getWidth(); x++) {
				list.add((byte)(bi.getRGB(x, y)));
			}
		}
		//return ArrayUtils.toPrimitive(Bytes.toArray(all));
		byte[] result = new byte[list.size()];
		for(int i = 0; i < list.size(); i++) {
		    result[i] = list.get(i).byteValue();
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		String image_path = args[0];
		String plaintext = args[1];
		File image_file = new File(image_path);
		BufferedImage bi = ImageIO.read(image_file);
		byte[] original = bufferedImageToBytes(bi);
		byte[] cipherimage = Steganography.encode(original, plaintext);
		System.out.println(Steganography.decode(cipherimage));
		
		
		
		//BufferedImage output = bytesToBufferedImage(cipherimage);
	}

	private static BufferedImage bytesToBufferedImage(byte[] cipherimage) {
		// TODO Auto-generated method stub
		return null;
	}
}
