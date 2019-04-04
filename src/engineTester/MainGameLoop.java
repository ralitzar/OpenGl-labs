package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Render;
import shaders.StaticShader;
import textures.Texture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Render render = new Render(shader);
		
		float[] vertices = {
				-0.5f,  0.5f, 0, //V0
				-0.5f, -0.5f, 0, //V1
				 0.5f, -0.5f, 0, //V2
				 0.5f,  0.5f, 0  //V3
		};
		
		int[] indices = {
				0,1,3, //top left triangle (V0,V1,V3)
				3,1,2 //bottom right triangle (V3,V1,V2)
		};
		
		float[] textureCoords = {
				0.0f, 0.0f, //V0 
				0.0f, 1.0f, //V1
				1.0f, 1.0f, //V2
				1.0f, 0.0f	//V3			
		};
		
		RawModel model = loader.loadToVAO(vertices,textureCoords, indices);
		Texture texture = new Texture(loader.loadTexture("image0")); /*constructor*/
		TexturedModel staticModel = new TexturedModel(model, texture);
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-1),0,0,0,1);
		
		while(!Display.isCloseRequested()) {
			
			entity.changePosition(0, 0, -0.1f);
			entity.rotateEntity(0,1,0);
			
			render.prepare();
			shader.start();
			
			// game logic
			render.render(entity,shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
