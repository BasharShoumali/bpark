package serverGUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientConnInfo {
    private final StringProperty hostName;
    private final StringProperty ipAddress;
    private final StringProperty status;

    public ClientConnInfo(String hostName, String ipAddress, String status) {
        this.hostName = new SimpleStringProperty(hostName);
        this.ipAddress = new SimpleStringProperty(ipAddress);
        this.status = new SimpleStringProperty(status);
    }

    // Property getters
    @SuppressWarnings("exports")
	public StringProperty hostNameProperty() { return hostName; }
    @SuppressWarnings("exports")
	public StringProperty ipAddressProperty() { return ipAddress; }
    @SuppressWarnings("exports")
	public StringProperty statusProperty() { return status; }

    // Regular getters
    public String getHostName() { return hostName.get(); }
    public String getIpAddress() { return ipAddress.get(); }
    public String getStatus() { return status.get(); }

    // Setter
    public void setStatus(String status) { this.status.set(status); }
}