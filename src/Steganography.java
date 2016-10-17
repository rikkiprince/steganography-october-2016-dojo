import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.imageio.ImageIO;

public class Steganography {

	public static byte[] encode(byte[] original, String plaintext) {
		for(int i=0; i<Math.min(16, original.length); i++)
			System.out.println("Byte #"+i+","+(int)original[i]);
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
		for(int i=0; i<Math.min(16, original.length); i++)
			System.out.println("Byte #"+i+","+(int)original[i]);
		return original;
	}
	
	public static String decode(byte[] cipherimage) throws UnsupportedEncodingException {
		for(int i=0; i<Math.min(16, cipherimage.length); i++)
			System.out.println("Byte #"+i+","+(int)cipherimage[i]);
		int image_byte = 0;
		StringBuilder builder = new StringBuilder();
		byte[] out = new byte[13];
		int character=0;
		while(image_byte < 1*8/*cipherimage.length*/) {
			byte new_character = 0;
			for(byte bit=0; bit<8; bit++) {
				byte lsb = (byte) (cipherimage[image_byte]&0x1);
				//System.out.print(lsb);
				new_character += lsb<<bit;
				image_byte++;
			}
			//System.out.println();
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
				int rgb = bi.getRGB(x, y);
				byte red = (byte) ((rgb & 0xFF0000) >> 16);
				byte green = (byte) ((rgb & 0x00FF00) >> 8);
				byte blue = (byte) ((rgb & 0x0000FF) >> 0);
				list.add(red);
				list.add(green);
				list.add(blue);
			}
		}

		byte[] result = new byte[list.size()];
		for(int i = 0; i < list.size(); i++) {
		    result[i] = list.get(i).byteValue();
		}
		return result;
	}

	public static BufferedImage bytesToBufferedImage(byte[] cipherimage, int width, int height) {
		System.out.println("Number of bytes: "+cipherimage.length);
		System.out.println("w x h x 3 = "+width+" x "+height+" x 3 = "+(width*height*3));
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				// http://stackoverflow.com/a/1143332/829305
				int index=y*width+x;
				int red=cipherimage[index] & 0xFF;
				int green=cipherimage[index+1] & 0xFF;
				int blue=cipherimage[index+2] & 0xFF;
				int rgb = (red << 16) | (green << 8) | blue;
				out.setRGB(x, y, rgb);
			}
		}

		return out;
	}
	
	public static void main(String[] args) throws IOException {
		String image_path = "cat.bmp";	//args[0];
		String plaintext = "cats are cool";	//args[1];
		File image_file = new File(image_path);
		BufferedImage bi = ImageIO.read(image_file);
		byte[] original = bufferedImageToBytes(bi);
		byte[] cipherimage = Steganography.encode(original, plaintext);
		System.out.println(Steganography.decode(cipherimage));
		
		
		
		//BufferedImage output = bytesToBufferedImage(cipherimage);
	}
}
