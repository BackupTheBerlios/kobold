/*
 * KoboldServerInterface.java
 *
 * Created on 31. Juli 2000, 22:57
 */

package KoboldServer;

/** Diese Klasse ist die Umsetzung der im Grobentwurf mit Interface
 * gekennzeichneten Komponente des Kobold-Servers. Ihre Methoden
 * werden vom Client mittels RPC-Aufrufe angesprochen.
 * @author Armin Cont
 */
public class KoboldServerInterface {
    
    /** Macht noch gar nix
     */
    public KoboldServerInterface() {
    }
    
    /** Vor dem Aufruf anderer Methoden des KoboldServerInterfaces
     * muss sich ein Client mittels dieser Funktion auf dem
     * Server anmelden.
     *
     * @param username Ein gültiger Benutzername
     * @param password Das zum Benutzernamen passende Passwort
     * @return Logininformationen (Rolleninfo, sessionID)
     * - Datenstruktur noch festzulegen
     */    
    public mixed login(String username, String password) {
    }
    
    /** Loggt einen eingeloggten Benutzer aus. Die übergebene
     * sessionID wird dabei ungültig.
     * @param sessionID gültige sessionID
     */    
    public void logout(String sessionID) {
    }
    
    /** Fragt die Daten des angegebenen Benutzers ab.
     * @param sessionID eine gültige sessionID
     * @param username Name des Benutzers, dessen Daten abgefragt werden sollen
     * @return Daten des Benutzers, falls dieser existiert -
     * Datenstruktur ist noch festzulegen
     */    
    public mixed getUserInfo(String sessionID, String username) {
    }
    
    /** Registriert einen Neuen Benutzer auf dem Server.
     * @param sessionID eine gültige Session-ID
     * @param info Benutzerinformationen des neu zu registrierenden
     * Benutzers - Datenstruktur noch festzulegen
     * @return true, falls bei der Registrierung kein Fehler aufgetreten
     * ist, false sonst.
     */    
    public boolean addUser(String sessionID, mixed info) {
    }
    
    /** Löscht einen registrierten Benutzer vom Server.
     * @param sessionID eine gültige Session-ID
     * @param username Name des zu entfernenden Benutzers
     * @return true, falls der angegebene Benutzer erfolgreich
     * entfernt werden konnte, false sonst.
     */    
    public boolean removeUser(String sessionID, String username){
    }
    
    /** Ändert die auf dem Server gespeicherten Benutzerdaten
     * @param sessionID eine gültige Session-ID
     * @param username Name des zu modifizierenden Benutzers
     * @param info noch festzulegen
     * @return true, falls die Modifikation erfolgreich durchgeführt
     * werden konnte, false sonst.
     */    
    public boolean modifyUser(String sessionID, String username, mixed info){
    }
    
    /** Liefert eine Liste aller auf dem Serevr registrierten Benutzernamen.
     * @param sessionID eine gültige Session-ID
     * @return Liste aller gültigen Benutzernamen
     */    
    public list getUserList(String sessionID){
    }
    
    /** Registriert eine neue Produktlinie auf dem Server.
     * @param sessionID eine gültige Session-ID
     * @param info Informationen zur neu zu erstellenden Produktlinie -
     * Datenstruktur noch festzulegen
     * @return true, falls die neue Produktlinie erfolgreich
     * registriert werden konnte, false sonst.
     */    
    public boolean addProductLine(String sessionID, mixed info){
    }
    
    /** entfernt die auf dem Server gespeicherten Informationen
     * zur angegebenen Produktlinie
     * @param sessionID eine gültige Session-ID
     * @param productline Name der zu entfernenden Produklinie
     * @return true, falls die angegebene Produktlinie erfolgreich
     * entfernt werden konnte, false sonst.
     */    
    public boolean removeProductLine(String sessionID, String productline){
    }
    
    /** Fragt die auf dem Server gespeicherten Informationen zur
     * angegebenen Produktlinie ab.
     * @param sessionID eine gültige Session-ID
     * @param productline Name der Produktlinie, deren Daten abgefragt werden
     * sollen.
     * @return Produktlinieninformationen - noch festzulegen
     */    
    public mixed getProductLineInfo(String sessionID, String productline){
    }
    
    /** Ändert die auf dem Server gespeicherten Daten der angegebenen
     * Produktlinie.
     * @param sessionID eine gültige Session-ID
     * @param productline Name der zu ändernden Produktlinie
     * @param info Änderungsinformationen - noch festzulegen
     * @return true, falls die Modifikation erfolgreich durchgeführt
     * werden konnte, false sonst.
     */    
    public boolean modifyProductLine(String sessionID, String productline, mixed info){
    }
    
    /** Liefert eine Liste der Namen aller auf dem Server registrierten
     * Produktlinien.
     * @param sessionID eine gültige Session-ID
     * @return Liste der Produktliniennamen
     */    
    public list getProductLineList(String sessionID){
    }
    
    /** Ersetzt den verantwortlichen PLE einer Produktlinie durch
     * einen neuen und aktualisiert die Rolleninformationen der
     * beiden Benutzer.
     * @param sessionID eine gültige Session-ID
     * @param productline Name der Produktlinie dessen PLE ersetzt werden soll
     * @param user Name des Benutzers, der die PLE-Rolle für die angegebene
     * Produktlinie übernehmen soll.
     */    
    public void setProductLineEngineer(String sessionID, String productline, String user){
    }
    

    /** Erweitert eine bestehende Produktlinie um ein weiteres
     * Produkt.
     * @param sessionID eine gültige Session-ID
     * @param productline Name der zu erweiternden Produktlinie
     * @param info Produktinformationen - noch festzulegen
     * @return true, falls die Modifikation erfolgreich durchgeführt
     * werden konnte, false sonst.
     */    
    public boolean addProduct(String sessionID, String productline, mixed info){
    }
    
    /** Entfernt ein Produkt aus einer Produktlinie.
     * @param sessionID eine gültige Session-ID
     * @param product Name des zu entfernenden Produkts
     * @return true, falls die Entfernung erfolgreich war, false sonst
     */    
    public boolean removeProduct(String sessionID, String product){
    }
    
    /** ruft Informationen zum angegebenen Produkt ab
     * @param sessionID eine gültige Session-ID
     * @param product Name des Produkts
     * @return Produktinformationen - noch festzulegen
     */    
    public mixed getProductInfo(String sessionID, String product){
    }
    
    /** Ändert die Produktdaten
     * @param sessionID eine gültige SessionID
     * @param product Name des Produkts, dessen Produktdaten geändert werden sollen
     * @param info Änderungsinformationen - noch festzulegen
     * @return true, falls die Änderung erfolgreich war,
     * false sonst.
     */    
    public boolean modifyProduct(String sessionID, String product, mixed info){
    }
    
    /** Liefert eine Liste der Namen aller in der angegebenen
     * Produktlinie enthaltenen Produkte.
     * @param sessionID eine gültige Session-ID
     * @param productline Name der Produktlinie
     * @return Liste der Produktnamen
     */    
    public list getProductList(String sessionID, String productline){
    }
    
    /** Weist die PE-Rolle des angegebenen Produkts einem neuen Benutzer zu.
     * @param sessionID eine gültige Session-ID
     * @param product Name des Produkts dessen PE ausgewechselt werden soll
     * @param user Benutzernamen des neuen PE
     * @return true, falls der neue PE erfolgreich zugewiesen wurde, false sonst.
     */    
    public boolean setProductEngineer(String sessionID, String product, String user){
    }
    
    /** weist dem angegebenen Produkt einen neuen Benutzer als
     * Programmierer zu und aktualisiert dessen Rollendaten
     * @param sessionID eine gültige Session-ID
     * @param product Name des Produkts, dem der neue Programmierer zugeordnet werden soll
     * @param user Benutzername des neuen Programmierers
     * @return true, falls der neue Programmierer erfolgreich
     * zugewiesen werden konnte, false sonst.
     */    
    public boolean addProgrammerToProduct(String sessionID, String product, String user){
    }
    
    /** Entfernt einen Programmierer vom angegebenen Produkt
     * @param sessionID eine gültige Session-ID
     * @param product Name des Produkts, von dem der angegeben Programmierer entfernt werden soll
     * @param user Benutzernamen des zu entfernenden Programmierers
     * @return true, falls der Programmierer erfolgreich entfernt
     * werden konnte, false sonst
     */    
    public boolean removeProgrammerFromProduct(String sessionID, String product, String user){
    }
    
    /** Liefert den Benutzernamen des PEs des angegebenen Produkts
     * @param sessionID gültige Session-ID
     * @param product Name des Produkts
     * @return Benutzername des PE - leerer String falls ein
     * Fehler aufgetreten ist
     */    
    public String getProductEngineer(String sessionID, String product){
    }
    
    /** Liefert eine Liste der Benutzernamen aller dem angegebenen
     * Produkt zugeordneten Programmierer
     * @param sessionID eine gültige Session-ID
     * @param product Name des Produkts
     * @return die Programmiererliste
     */    
    public list geProgrammerList(String sessionID, String product){
    }
    
    /** Übergibt ein KoboldMessage-Objekt an den Server zur weiteren
     * Verarbeitung.
     * @param sessionID eine gültige Session-ID
     * @param message Instanz einer von KoboldMessage abgeleiteten
     * Kindklasse  (Action-, Workflow-, TextMessage).
     */    
    public void sendMessage(String sessionID, KoboldMessage message){
    }
    
    /** Liefert die aktuelle MessageQueue des mittels sessionID
     * bezeichneten Benutzers.
     * @param sessionID eine gültige sessionID
     * @return die MessageQueue des Benutzers, der der übergebenen
     * Session-ID zugeordnet ist
     */    
    public MessageQueue getMessages(String sessionID){
    }
}
