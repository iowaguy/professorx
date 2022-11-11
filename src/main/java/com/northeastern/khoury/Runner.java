package com.northeastern.khoury;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Runner {
  static Logger logger = LogManager.getLogger(Runner.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.printf("%d arguments provided. Need 1.", args.length);
      System.exit(1);
    }

    PolicyEngine pe = new PolicyEngine(args[0]);
    boolean b = pe.getPermission("U1", "O1", "permission0");
    System.out.println("Can U1 do permission0 on O1? " + b);

    b = pe.getPermission("NO", "O1", "permission0");
    System.out.println("Can NO do permission0 on O1? " + b);
  }
}
