import java.awt.Color;
import java.io.Serializable;


/**
 * This Class encapsulates Rewards
 *
 */
public class Reward implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = -8525646557795670790L;

	/**Enum type for the different kinds of rewards	 */
	public static enum Types{SHIELD, PROBE, BONUS, IMMORTAL};
	/**enum type attribute
	 * 
	 */
	private Types type;
	/**color attribute 
	 * 
	 */
	private Color color;
	
	/**Constructor
	 * @param type
	 */
	public Reward(Types type){
		this.setType(type);
		//TODO Determine color codes for rewards
	}

	/**
	 * @return
	 */
	public Types getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(Types type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
}
