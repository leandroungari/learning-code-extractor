import json
import csv

class File:

  def __init__(self, path):
    self.path = path

  def read(self):
    with open(self.path, "r") as file:
      if self.path.endswith(".json"):
        return json.load(file)
      elif self.path.endswith(".csv"):
        result = []
        reader = csv.DictReader(file)
        for row in reader:
          result.append(row)
        return result
        
    return None

  def write(self, data):
    with open(self.path, "w") as file:
      if self.path.endswith(".json"):
        json.dump(data, file, indent = 2, sort_keys=True)
        print(f"> write {self.path}")

import os

class Folder:

  def __init__(self, base):
    self.base = base

  def all_files(self):
    result = []
    self.__all_files_recursive(result, self.base)
    return result

  def __all_files_recursive(self, result, folder):
    for file in os.listdir(folder):
      if os.path.isdir(f"{folder}/{file}"):
        self.__all_files_recursive(result, f"{folder}/{file}")
      else: 
        filepath = os.path.join(folder, file)
        result.append(filepath)
        

  def all_files_by_ext(self, ext=".json"):
    result = []
    for file in self.all_files():
      if file.endswith(ext):
        result.append(file)
    return result

  def all_files_with_pattern(self, pattern):
    result = []
    for file in self.all_files():
      if pattern in file:
        result.append(file)
    return result
  
  def file(self, path):
    return File(path)

  def create(self):
    if not os.path.exists(self.base):
      os.makedirs(self.base)

  