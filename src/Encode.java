import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Encode {
	public static void main(String[] args) throws IOException {
		if(args.length < 2) {
			System.out.println("Usage: java Encode <image_path> <plaintext>");
			System.exit(0);
		}
		String image_path = args[0];
		String plaintext = args[1];
		
		// read image file into byte array
		BufferedImage bi = ImageIO.read(new File(image_path));
		byte[] original = Steganography.bufferedImageToBytes(bi);
		
		// put text into image
		byte[] cipherimage = Steganography.encode(original, plaintext);
		
		// write image back to PNG
		BufferedImage bo = Steganography.bytesToBufferedImage(cipherimage, bi.getWidth(), bi.getHeight());
		//BufferedImage bo = Steganography.bytesToBufferedImage(original, bi.getWidth(), bi.getHeight());
		ImageIO.write(bo, "bmp", new File("cipher_"+image_path+".bmp"));
	}
}
