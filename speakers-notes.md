`git cherry-pick -n demo-init`

## TODO: 1 (`GameController`)
```
@RestController
```

## TODO: 2 (`GameController`)
```
@RequestMapping(method = RequestMethod.POST, value = "")
```

## TODO: 3 (`GameController`)
```
@RequestMapping(method = RequestMethod.GET, value = "/{game}", produces = MediaType.APPLICATION_JSON_VALUE)
```

## TODO: 4 (`GameController`)
```
@RequestMapping(method = RequestMethod.DELETE, value = "/{game}")
```

## TODO: 5 (`GameController`)
```
return new ResponseEntity<>(headers, HttpStatus.CREATED);
```

## TODO: 6 (`IllegalTransitionException`)
```
@ResponseStatus(HttpStatus.CONFLICT)
```

## TODO: 7 (`ExceptionHandling`)
```
@ExceptionHandler(IllegalArgumentException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
```

## TODO: 8 (`GameController`)
```
headers.setLocation(linkTo(GameController.class).slash(game).toUri());
```

## TODO: 9 (`GameResourceAssembler`)
```
resource.add(linkTo(DoorController.class, game.getId()).slash(door).withRel("door"));
```

## TODO: 10 (`DoorControllerTest`)
```
.andExpect(jsonPath("$.content").value("UNKNOWN"))
```
