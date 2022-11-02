/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Michael Ladderbush
 * service error is used to collect all throws and return an appropriate exception.
 */
public class serviceError extends Exception {
    
    public serviceError(String errorMessage) {
        
        super(errorMessage);
        
    }
    
    public void ParserError() {
        
    }
    
}
