package pl.kacorvixon.blue.util.shader;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programID;

    private final int program;

    public Shader(String fragmentShaderLoc) {
        program = glCreateProgram();
        String vertexLoc;
        vertexLoc = "#version 120\n" +
                "\n" +
                "void main() {\n" +
                "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
                "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
                "}";
        glAttachShader(this.program, createShader(fragmentShaderLoc, GL_FRAGMENT_SHADER));
        glAttachShader(this.program, createShader(vertexLoc, GL_VERTEX_SHADER));
        glLinkProgram(program);
        this.programID = program;
    }

    public void startProgram() {
        glUseProgram(programID);
    }

    public int getProgramID() {
        return programID;
    }

    public void stopProgram() {
        glUseProgram(0);
    }

    public int getUniformLoc(String uniform) {
        return glGetUniformLocation(programID, uniform);
    }

    private int createShader(String oskar, int shaderType) {
        final int shader;
        shader = glCreateShader(shaderType);
        glShaderSource(shader, oskar);
        glCompileShader(shader);
        return shader;
    }
}
