
# Interface pour le repository
from abc import ABC, abstractmethod

from logic.models.user import User


class IUserRepository(ABC):
    @abstractmethod
    def register(self, user: User):
        pass

    @abstractmethod
    def login(self, mail: str, password: str):
        pass