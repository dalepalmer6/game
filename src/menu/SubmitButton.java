package menu;

public class SubmitButton extends MenuItem {
	private String output = "";
	
	public void setOutput(String s) {
		this.output = s;
	}
	
	public SubmitButton(StartupNew m) {
		super("Done",0,0,m);
	}
	
	public String execute() {
		Menu m = state.getMenuStack().pop();
		setOutput(m.getInput());
		m = state.getMenuStack().peek();
//		m = state.getMenuStack().pop();
//		state.getMenuStack().push(m);
//		MenuItem c = state.getSelectionStack().pop(); //gets rid of the Submit click
//		c = state.getSelectionStack().pop();
//		state.getSelectionStack().push(c);
//		c.setText(m.getInput());
		m.setInput(output);
//		new BackButton(state).execute();
		return null;
	}
}
