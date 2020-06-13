package com.nammaexpo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResource {

  @GetMapping("/exhibitor")
  @PreAuthorize("hasAuthority('EXHIBITOR')")
  public String exhibitor(
      @RequestHeader(value = "Authorization") String authorization
  ) {
    return "Exhibitor";
  }

  @GetMapping("/visitor")
  @PreAuthorize("hasAuthority('VISITOR')")
  public String visitor(
      @RequestHeader(value = "Authorization") String authorization
  ) {
    return "Visitor";
  }

}
