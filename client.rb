#!/usr/bin/env ruby

require 'json'
require 'rest_client'

GAME_ROOT = 'http://localhost:8080/games'

def get_doors(game)
  links = JSON.parse(RestClient.get(game).to_str)['links']
  links.select { |link| link['rel'] == 'door' }.map { |link| link['href'] }
end

def print_state(game, doors)
  puts
  puts "Game Status: #{JSON.parse(RestClient.get(game).to_str)['status']}"

  doors.each_with_index do |door, i|
    payload = JSON.parse(RestClient.get(door).to_str)
    puts "  Door #{i} Status: #{payload['status']}/#{payload['content']}"
  end
end

def update_door(door, status)
  RestClient.put door, {:status => status}.to_json, :content_type => :json
end

################################################################################

game = RestClient.post(GAME_ROOT, {}).headers[:location]
doors = get_doors game

puts "Let's Make a Deal!"
print_state game, doors

print 'Select a door (0, 1, or 2)... '
update_door doors[ARGF.gets.to_i], 'SELECTED'
print_state game, doors

print 'Open a door (0, 1, or 2)... '
update_door doors[ARGF.gets.to_i], 'OPENED'
print_state game, doors

RestClient.delete game
puts
puts "Cleaned up"
