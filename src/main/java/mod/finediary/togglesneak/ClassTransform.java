package mod.finediary.togglesneak;

import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import scala.tools.asm.ClassReader;
import scala.tools.asm.ClassWriter;
import scala.tools.asm.Opcodes;
import scala.tools.asm.tree.AbstractInsnNode;
import scala.tools.asm.tree.ClassNode;
import scala.tools.asm.tree.FieldInsnNode;
import scala.tools.asm.tree.MethodInsnNode;
import scala.tools.asm.tree.MethodNode;

public class ClassTransform implements IClassTransformer, Opcodes{
	private String target = "net.minecraft.util.MovementInputFromOptions";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		try{
			if(FMLLaunchHandler.side().equals(Side.CLIENT)){
				if (name.equals(target)) {
					return patch(basicClass, false, name);
				} else if (transformedName.equals(target)){
					return patch(basicClass, true, name);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("failed to find the class file : '&a' ! Please make sure your forge is an appropriate version.".replaceAll("&a", target), e);
		}
		return basicClass;
	}

	private byte[] patch(byte[] basicClass, boolean isObfuscated, String name){
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);

		MethodNode targetMethod = null;

		List<MethodNode> methods = classNode.methods;

		String targetMethodName = isObfuscated ? "a" : "updatePlayerMoveState";

		for(MethodNode method: methods) {
			//SpyDebugLog.debug(method.name);
			if(method.name.equals(targetMethodName)) {
				targetMethod = method;
				break;
			}
		}

		if (targetMethod == null) {
			TSLogger.info("Couldn't find the method : '&a' ! Please make sure your forge is an appropriate version.".replaceAll("&a", targetMethodName));
			return basicClass;
		}

		String targetNodeName = isObfuscated ? "V" : "keyBindSneak";

		AbstractInsnNode target = null;

		for (AbstractInsnNode node : targetMethod.instructions.toArray()){
			if (node.getOpcode() == INVOKEVIRTUAL){
				if (node.getPrevious() instanceof FieldInsnNode) {
					TSLogger.debug(((FieldInsnNode)node.getPrevious()).name);
					if(((FieldInsnNode)node.getPrevious()).name.equals(targetNodeName)) {
						target = node;
					}
				}
			}
		}

		if (target == null){
			TSLogger.info("Couldn't find the node: '&a' ! Please make sure your forge is an appropriate version.".replaceAll("&a", targetNodeName));
			return basicClass;
		}

		MethodInsnNode insertionNode = new MethodInsnNode(INVOKESTATIC, "mod/finediary/togglesneak/ClassTransform", "InvokeMethod", "(Z)Z");
		targetMethod.instructions.insert(target, insertionNode);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		TSLogger.info("Initialization of Toggle Sneak Mod has completed.");

		return writer.toByteArray();
	}

	static int ticks = 0;
	static boolean isSneakBeforeTick = false;
	static boolean toggleMode = false;

	public static boolean InvokeMethod(boolean isSneakKeyDown){
		if (toggleMode) {
			ticks++;
		} else {
			ticks = 0;
		}
		if (!isSneakBeforeTick && isSneakKeyDown){
			toggleMode = !toggleMode;
		} else if (isSneakBeforeTick && !isSneakKeyDown){
			if (toggleMode){
				if (ticks > 5){
					toggleMode = false;
				}
			}
		}
		isSneakBeforeTick = isSneakKeyDown;
		return toggleMode;
	}
}
