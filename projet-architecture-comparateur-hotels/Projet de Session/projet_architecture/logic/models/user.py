class User:
    def __init__(self, username: str, mail: str, password: str, id: str = None):
        self.id = id
        self.username = username
        self.mail = mail
        self.password = password

    def to_dict(self):
        return {
            # "_id": self.id,
            "username": self.username,
            "mail": self.mail,
            "password": self.password,  # Note: le mot de passe est déjà hashé
        }

    @staticmethod
    def from_dict(data: dict):
        return User(
            id=str(data.get("_id")),
            username=data.get("username"),
            mail=data.get("mail"),
            password=data.get("password"),
        )
