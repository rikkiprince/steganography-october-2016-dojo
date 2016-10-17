import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

public class Decode {
	public static void main(String[] args) throws IOException {
		if(args.length < 1) {
			System.out.println("Usage: java Decode <image_path>");
			System.exit(0);
		}
		String image_path = args[0];
		
		// read cipher image file into byte array
		BufferedImage bi = ImageIO.read(new File(image_path));
		byte[] cipherimage = Steganography.bufferedImageToBytes(bi);
		
		// extract text from image
		String plaintext = Steganography.decode(cipherimage);
		
		// print out text
		System.out.println(plaintext);
	}
}
