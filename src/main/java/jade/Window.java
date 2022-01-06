package jade;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private int width, height;
    private String title;
    private static Window window;
    private long glfwWindow;
    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Engine";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get() {
        if(Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public void run() {

        System.out.println("Hello LWJGL " + Version.getVersion() + " I");

        init();
        loop();

        //Free the memory once the loop exits
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
    }

    public void init(){
        //Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initiaize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW Window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //Make the Window visible
        glfwShowWindow(glfwWindow);

		/*
		This line is critical for LWJGL's interoperation with GLFW's
		OpenGL context, or any context that is managed externally.
		LWGL detects the context that is current in the current thread,
		creates the GLCapabilities instance and makes the OpenGL
		bindings available for use.
		 */
        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(fadeToBlack){
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b - 0.01f, 0);
            }
            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack = true;
            }

            glfwSwapBuffers(glfwWindow);
        }
    }


}