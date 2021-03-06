package io.github.armenari.rexaetheres.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	
	public static Texture ui_pieces = loadTexture("/assets/ui_pieces.png");
	public static Texture default_font = loadTexture("/assets/default_font.png");
	public static Texture character = loadTexture("/assets/char.png");
	
	public static Texture shadow = loadTexture("/assets/shadow.png");
	
	public static Texture potions = loadTexture("/assets/items/Potion.png");
	public static Texture amulets = loadTexture("/assets/items/Amulet.png");
	public static Texture scrolls = loadTexture("/assets/items/Scroll.png");
	public static Texture hint_paper = loadTexture("/assets/items/hint_paper.png");
	
	public static Texture dbox = loadTexture("/assets/dialog_box.png");
	
	private int width, height;
	private int id;

	public Texture(int width, int height, int id) {
		this.width = width;
		this.height = height;
		this.setId(id);
	}

	public static Texture loadTexture(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Texture.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int w = image.getWidth();
		int h = image.getHeight();

		int[] pixels = new int[w * h];
		image.getRGB(0, 0, w, h, pixels, 0, w);

		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixel = pixels[x + y * w];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) ((pixel) & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();

		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		return new Texture(w, h, id);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, getId());
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}