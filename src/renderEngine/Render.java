package renderEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import utilities.Mathematic;

public class Render {
	
	private static final float POV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private Matrix4f projectionMatrix;
	
	public Render(StaticShader shader) {
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	/* render function for entity */

	 public void render(Entity entity, StaticShader shader){
		 TexturedModel model = entity.getModel();
		 RawModel rawModel = model.getRawModel();
			 GL30.glBindVertexArray(rawModel.getVaoID());
			 GL20.glEnableVertexAttribArray(0);
			 GL20.glEnableVertexAttribArray(1);
			 Matrix4f trasformationMatrix = Mathematic.createTransformation(entity.getPosition(), 
					 entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
			 shader.loadTransformationMatrix(trasformationMatrix);
			 GL13.glActiveTexture(GL13.GL_TEXTURE0);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
			 GL11.glDrawElements(GL11.GL_TRIANGLES,rawModel.getVetexCount(),GL11.GL_UNSIGNED_INT,0);
			 GL20.glDisableVertexAttribArray(0);
			 GL20.glDisableVertexAttribArray(1);
			 GL30.glBindVertexArray(0);
			 GL30.glBindVertexArray(1);
	 }
	 
	 private void createProjectionMatrix() {
		 float aspectRatio = (float)Display.getWidth()/(float)Display.getHeight();
		 float yScale = (float) ((1f/Math.tan(Math.toRadians(POV/2f))) * aspectRatio);
		 float xScale = yScale / aspectRatio;
		 float frustum = FAR_PLANE - NEAR_PLANE;
		 
		 projectionMatrix = new Matrix4f();
		 projectionMatrix.m00 = xScale;
		 projectionMatrix.m11 = yScale;
		 projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum);
		 projectionMatrix.m23 = -1;
		 projectionMatrix.m32 = -((2*NEAR_PLANE*FAR_PLANE)/frustum);
		 projectionMatrix.m33 = 0;
	 }
	}
